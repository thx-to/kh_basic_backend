package com.kh.spring_jpa.service;

import com.kh.spring_jpa.dto.BoardReqDto;
import com.kh.spring_jpa.dto.BoardResDto;
import com.kh.spring_jpa.dto.CommentResDto;
import com.kh.spring_jpa.entity.Board;
import com.kh.spring_jpa.entity.Comment;
import com.kh.spring_jpa.entity.Member;
import com.kh.spring_jpa.repository.BoardRepository;
import com.kh.spring_jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 게시글 등록
    // 트랜잭션은 하나의 논리적인 작업 단위, 그 중에서 하나라도 실패하면 롤백이 일어남
    // RequestDto통해서 정보가 들어옴 (컨트롤러가 받아서)
    @Transactional
    public boolean saveBoard(BoardReqDto boardReqDto) {

        try {

            // 프론트엔드에 전달한 이메일로 회원 정보를 가져옴 (이메일로 Member 정보를 뽑아옴)
            // 올릴때는 이메일밖에 안들어가지만 DB에 넣을 떄는 연관 관계 매핑으로 같이 올려줘야 함
            Member member = memberRepository.findByEmail(boardReqDto.getEmail())
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));

            // 저장하기 위한 빈 엔티티 객체를 만듦
            Board board = new Board();
            board.setTitle(boardReqDto.getTitle());
            board.setContent(boardReqDto.getContent());
            board.setImgPath(boardReqDto.getImgPath());
            // Member 객체를 통째로 넣어줌
            board.setMember(member);

            // 실제 INSERT
            // BoardRepository에 넣어주기
            boardRepository.save(board);

            return true;

        } catch (Exception e) {

            log.error("게시글 등록 실패 : {}", e.getMessage());
            return false;

        }

    }


    // 게시글 상세 조회
    public BoardResDto findByBoardId(Long id) {

        // 해당 보드(게시글)가 있는지를 먼저 찾기
        // 쿼리가 하나(findById)이기 때문에 트랜잭션을 걸지 않아도 됨
        // 예외 처리는 orElseThrow로 걸어서 try ~ catch 구문 생략
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("해당 게시글이 존재하지 않습니다."));

//        // 게시글이 있으면 해당 정보를 빼와서 넘겨줌
//        // 빈 객체를 만들고 내용 넣기
//        BoardResDto boardResDto = new BoardResDto();
//        boardResDto.setBoardId(board.getId());
//        boardResDto.setTitle(board.getTitle());
//        boardResDto.setContent(board.getContent());
//        boardResDto.setImgPath(board.getImgPath());

        // getMember()는 참조 객체
        // 참조 객체 내에 있는 인스턴스 필드 getEmail()을 가져와야 하기 때문에 get을 한번 더 걸어줌
//        boardResDto.setEmail(board.getMember().getEmail());

//        return boardResDto;

        // 아래 엔티티를 DTO로 변환해주는 메소드를 불러와서 리턴문을 이렇게 써줘도 됨
        // 쓰려면 위에 BoardResDto ~ getImgPath까지 막기
         return convertEntityToDtoWithoutComments(board);

    }


    // 게시글 전체 조회
    public List<BoardResDto> findAllBoard() {

        // 데이터베이스에 있는 모든 게시글 가져오기
        List<Board> boards = boardRepository.findAll();

        // 가져온 게시글을 프론트엔드가 전달하려면 엔티티를 dto로 바꿔주는 작업이 필요
        // 빈 ArrayList를 만들어서 넣어주기
        List<BoardResDto> boardResDtoList = new ArrayList<>();

        // add로 삽입하려면 boards를 db에 있는 개수만큼 순회하면서 하나씩 넣어줘야 함
        for (Board board : boards) {

//            BoardResDto boardResDto = new BoardResDto();
//            boardResDto.setBoardId(board.getId());
//            boardResDto.setTitle(board.getTitle());
//            boardResDto.setContent(board.getContent());
//            boardResDto.setImgPath(board.getImgPath());
//            boardResDto.setEmail(board.getMember().getEmail());
//            boardResDto.setRegDate(board.getRegDate());

            // 다 돌고 boardResDtoList에 add
            // 아래 만든, 엔티티를 DTO로 변환해주는 메소드를 불러와서 쓰기
            // convertEntityToDto를 통해서 BoardResDto를 반환받아서 리스트에 추가
            boardResDtoList.add(convertEntityToDtoWithoutComments(board));
        }
        return boardResDtoList;
    }


    // 게시글 검색
    public List<BoardResDto> searchBoard(String keyword) {
        List<Board> boards = boardRepository.findByTitleContaining(keyword);
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            boardResDtoList.add(convertEntityToDto(board));
        }
        return boardResDtoList;
    }


    // 게시글 페이징을 위한 페이지 수 조회 (선행 작업)
    // 사이즈 설정은 프론트가 함
    // 게시글 화면에 처음 들어갈 때만 불러주고 끝
    public int getBoardPageCount(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return boardRepository.findAll(pageRequest).getTotalPages();
    }

    // 게시글 페이징 (페이지 단위로 나누기)
    // 페이지네이션을 위해 가장 먼저 알아야 할 것은 총 게시글의 개수
    // 그리고 페이지당 게시글을 몇개씩 보여줄지 결정
    // 총 몇페이지로 구성되는지 백에게 물어보는 동작 필요
    // 페이지 이동은 이 메소드로만
    public List<BoardResDto> pagingBoardList(int page, int size) {

        // 전체가 넘어오지 않고, 이 부분에 대한 정보만 넘어옴
        // 몇번째 페이지의 몇개만 보여달라는 요청이 가능 (일부만 잘려서 넘어옴)
        Pageable pageable = PageRequest.of(page, size);

        // 해당하는 페이지의 게시글 리스트만 넘어옴
        List<Board> boards = boardRepository.findAll(pageable).getContent();

        // 빈 리스트 만들고 순회해서 add로 DTO 하나씩 넣어주기
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            boardResDtoList.add(convertEntityToDtoWithoutComments(board));
        }
        return boardResDtoList;
    }


    // 게시글 삭제
    public boolean deleteBoard(Long id, String email) {
        try {
            Board board = boardRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("해당 게시글이 존재하지 않습니다."));

            if (board.getMember().getEmail().equals(email)) {
                boardRepository.delete(board);
                log.info("{}님의 게시글이 성공적으로 삭제되었습니다.", board.getMember().getEmail());
                return true;
            } else {
                log.error("게시글은 작성자만 삭제할 수 있습니다.");
                return false;
            }
        } catch (Exception e) {
            log.error("게시글 삭제 실패 : {}", e.getMessage());
            return false;
        }
    }


    // 게시글 수정
    public boolean modifyBoard(Long id, BoardReqDto boardReqDto) {
        try {
            Board board = boardRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("해당 게시글이 존재하지 않습니다."));

            Member member = memberRepository.findByEmail(boardReqDto.getEmail())
                    .orElseThrow(()->new RuntimeException("해당 회원이 존재하지 않습니다."));

            if (board.getMember().getEmail().equals(boardReqDto.getEmail())) {
                board.setTitle(boardReqDto.getTitle());
                board.setContent(boardReqDto.getContent());
                board.setImgPath(boardReqDto.getImgPath());
                boardRepository.save(board); // UPDATE
                return true;
            } else {
                log.error("게시글은 작성자만 수정할 수 있습니다.");
                return false;
            }
        } catch (Exception e) {
            log.error("게시글 수정 실패 : {}", e.getMessage());
            return false;
        }
    }


    // 게시글 검색 (제목 OR 내용)
    public List<BoardResDto> searchSpecificBoard(String title, String content) {

        List<Board> boards = boardRepository.findByTitleContainingOrContentContaining(title, content);

        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for(Board board : boards) {
            boardResDtoList.add(convertEntityToDto(board));
        }
        return boardResDtoList;
    }

//    // 반복적인 부분(ENTITY를 DTO로 변환) 따로 빼서 사용해주기
//    private BoardResDto convertEntityToDto(Board board) {
//        BoardResDto boardResDto = new BoardResDto();
//        boardResDto.setBoardId(board.getId());
//        boardResDto.setTitle(board.getTitle());
//        boardResDto.setContent(board.getContent());
//        boardResDto.setImgPath(board.getImgPath());
//        boardResDto.setEmail(board.getMember().getEmail());
//        boardResDto.setRegDate(board.getRegDate());
//        return boardResDto;
//    }

    // 댓글 목록 조회
    public List<CommentResDto> commentList(Long boardId) {
        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(()-> new RuntimeException("해당 게시글이 존재하지 않습니다."));

            List<CommentResDto> commentResDtoList = new ArrayList<>();

            // Board 엔티티에서 OneToMany로 매핑해준 Comment 타입의 List
            for (Comment comment : board.getComments()) {
                CommentResDto commentResDto = new CommentResDto();
                commentResDto.setEmail(comment.getMember().getEmail());
                commentResDto.setBoardId(comment.getBoard().getId());
                commentResDto.setCommentId(comment.getCommentId());
                commentResDto.setContent(comment.getContent());
                commentResDto.setRegDate(comment.getRegDate());
                commentResDtoList.add(commentResDto);
            }
            return commentResDtoList;
        } catch (Exception e) {
            log.error("댓글 조회 실패 : {}", e.getMessage());
            return null;
        }
    }

    // 엔티티를 Dto로 변환하는 과정에서 댓글을 추가함
    private BoardResDto convertEntityToDto(Board board) {

        BoardResDto boardResDto = new BoardResDto();

        boardResDto.setBoardId(board.getId());
        boardResDto.setTitle(board.getTitle());
        boardResDto.setContent(board.getContent());
        boardResDto.setImgPath(board.getImgPath());
        boardResDto.setEmail(board.getMember().getEmail());
        boardResDto.setRegDate(board.getRegDate());

        List<CommentResDto> commentResDtoList = new ArrayList<>();
        for (Comment comment : board.getComments()) {
            CommentResDto commentResDto = new CommentResDto();

            commentResDto.setEmail(comment.getMember().getEmail());
            commentResDto.setBoardId(comment.getBoard().getId());
            commentResDto.setCommentId(comment.getCommentId());
            commentResDto.setContent(comment.getContent());
            commentResDto.setRegDate(comment.getRegDate());
            commentResDtoList.add(commentResDto);
        }
        boardResDto.setComments(commentResDtoList);
        return boardResDto;
    }


    // 댓글 제외 DTO (전체 글 보기 등에서는 댓글 출력 안되게)
    private BoardResDto convertEntityToDtoWithoutComments(Board board) {

        BoardResDto boardResDto = new BoardResDto();

        boardResDto.setBoardId(board.getId());
        boardResDto.setTitle(board.getTitle());
        boardResDto.setContent(board.getContent());
        boardResDto.setImgPath(board.getImgPath());
        boardResDto.setEmail(board.getMember().getEmail());
        boardResDto.setRegDate(board.getRegDate());

        boardResDto.setComments(new ArrayList<>());

        return boardResDto;
    }

}
