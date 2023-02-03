package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
/**
 * 토론 생성 API ([POST] /api/v1/debates) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
@ApiModel("DebateRegisterPostReq")
public class DebateRegisterPostReq {

	@ApiModelProperty(name="방장의 고유 ID")
	Long ownerId;

	@ApiModelProperty(name="토론 카테고리 ID")
	Long category;

	@ApiModelProperty(name="토론 생성시간", example="2022-01-11 13:00:00")
	LocalDateTime insertedTime;

	@ApiModelProperty(name="토론 시작시간", example="2022-01-11 13:00:00")
	LocalDateTime callStartTime;

	@ApiModelProperty(name="토론 종료시간", example="2022-01-11 14:00:00")
	LocalDateTime callEndTime;

	@ApiModelProperty(name="썸네일 URL")
	String thumbnailUrl;

	@ApiModelProperty(name="토론 이름")
	String title;

	@ApiModelProperty(name="토론 설명")
	String description;

	@ApiModelProperty(name="토론 활성화 상태")
	String state;

	@ApiModelProperty(name="토론 모드")
	String debateMode;

	@ApiModelProperty(name="사회자 참여 여부")
	Boolean moderatorOnOff;

	@ApiModelProperty(name="토론 발표 순서")
	ArrayList<DebateOrderBase> debateOrderList;

	@ApiModelProperty(name="토론 시간 설정")
	ArrayList<DebateTimeSettingBase> debateTimeSettingList;

	@ApiModelProperty(name="관점 리스트")
	ArrayList<PerspectiveBase> PerspectiveList;
}
