package com.terry.xplatform.vo;

import org.apache.ibatis.type.Alias;

import com.terry.xplatform.config.xplatform.data.XplatformVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("SampleVO")
public class SampleVO extends XplatformVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4891478433457788136L;

	/** 아이디 */
    private Integer id;
    
    /** 이름 */
    private String name;
    
    /** 내용 */
    private String description;
    
    /** 사용여부 */
    private String useYn;
    
    /** 등록자 */
    private String regUser;
}
