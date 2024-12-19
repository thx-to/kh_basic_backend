package com.kh.spring_jpa.service;

import com.kh.spring_jpa.dto.BoardReqDto;
import com.kh.spring_jpa.dto.BoardResDto;
import com.kh.spring_jpa.entity.Board;
import com.kh.spring_jpa.entity.Member;
import com.kh.spring_jpa.repository.BoardRepository;
import com.kh.spring_jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // 게시글이 있으면 해당 정보를 빼와서 넘겨줌
        // 빈 객체를 만들고 내용 넣기
        BoardResDto boardResDto = new BoardResDto();
        boardResDto.setBoardId(board.getId());
        boardResDto.setTitle(board.getTitle());
        boardResDto.setContent(board.getContent());
        boardResDto.setImgPath(board.getImgPath());

        // getMember()는 참조 객체
        // 참조 객체 내에 있는 인스턴스 필드 getEmail()을 가져와야 하기 때문에 get을 한번 더 걸어줌
        boardResDto.setEmail(board.getMember().getEmail());

        return boardResDto;

    }

}
