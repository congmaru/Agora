package com.ssafy.api.service;

import com.ssafy.api.request.DebateSearchAllGetReq;
import com.ssafy.api.request.DebateRegisterPostReq;
import com.ssafy.entity.rdbms.Debate;
import org.springframework.data.domain.Page;

/**
 *	토론 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface DebateService {
	Debate createDebate(DebateRegisterPostReq debateRegisterPostReq);

	Page<Debate> searchAll(DebateSearchAllGetReq debateReq);

	void deleteDebate(Long id);
}
