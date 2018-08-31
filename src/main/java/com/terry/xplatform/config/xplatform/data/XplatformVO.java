package com.terry.xplatform.config.xplatform.data;

import com.terry.xplatform.vo.SampleDefaultVO;

import lombok.Getter;
import lombok.Setter;

/**
 * XPlatform의 DataSet에서 변환되는 VO는 이 클래스를 반드시 상속받아야 한다
 * @author Terry Chang
 *
 */
@Getter
@Setter
public abstract class XplatformVO extends SampleDefaultVO {
	
	private int rowType;
	private boolean storeDataChanges;

}
