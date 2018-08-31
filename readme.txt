살펴봐야 할 내용을 정리한겁니다..
여기 문서에 적힌 대로 셋팅해보시고 테스트 꼭 진행하세요..
크게는 2가지 내용에 대해 설명할껍니다..
먼저 첫번째로 Mapper 인터페이스로 DAO를 대체하는 방법은..

1. 먼저 mybatis mapper 파일을 보세요..
제가 드린 소스를 기준으로 설명드리면 src/main/resources/com/terry/xplatform/resources/mybatis/mapper 디렉토리에 있는 sample.xml 입니다..
그 파일을 열어보세요..
그 파일을 보면 <mapper namespace="com.terry.xplatform.dao.SampleDAO"> 요렇게 있을겁니다..
즉 이 파일은 com.terry.xplatform.dao 패키지에 있는 SampleDAO 인터페이스와 매핑되는거에요..
그리고 현재 열어놓은 xml 파일을 보시면 insert, update 태그들이 보이실겁니다..이때 보셔야 할 것이 id 부분이에요..
왜냐면 이 아이디와 동일한 이름의 메소드를 만들어야 합니다..
일단 이렇게만 기억해두시고 com.terry.xplatform.dao 패키지에 있는 SampleDAO 인터페이스 파일을 열어보세요..
인터페이스이기 때문에 구체적으로 코딩된 내용은 없고 메소드의 정의만 있습니다..

@Mapper
public interface SampleDAO {

	public void insertSample(SampleVO sampleVO);
	public void updateSample(SampleVO sampleVO);
	public void deleteSample(@Param("id") int id);
	public SampleVO selectSample(@Param("id") int id);
	public List<SampleVO> selectSampleList(SampleDefaultVO sampleDefaultVO);
	public long selectSampleListTotCnt(SampleDefaultVO sampleDefaultVO);
	public Map<String, String> dataType();
	
}

여기서 보셔야 할 것은 @Mapper 인터페이스에요..이걸 적어줘야 합니다..
그리고 메소드 이름들을 보세요..위에서 언급했던 sample.xml 에 있는 각 태그들의 id와 일치되죠?
즉 해당 태그의 id에 대한 부분을 같은 이름의 메소드가 처리한다고 보시면 됩니다..
query 가 있는 xml을 저는 VO로 하기 때문에 메소드의 파라미터들을 VO로 받았지만 SQL 작업을 하면 VO로 받지 않고 int 값 같은 단일 타입으로 받아야 할 때도 있습니다..
그때 사용되는게 @Param 어노테이션입니다..

public void deleteSample(@Param("id") int id); 이걸 기준으로 설명드리면 얘와 매핑되는 것은

<delete id="deleteSample" parameterType="int">
	<![CDATA[
		DELETE FROM SAMPLE 
		WHERE ID=#{id}
	]]>
</delete>

이것과 매핑되겠죠? 여기서 보시면 parameterType 속성을 vo의 alias가 아닌 int 타입으로 줬습니다..
그렇기땜에 #{id}는 특정 VO의 id란 이름의 멤버변수가 아니에요..
이럴때 사용되는게 @Param 어노테이션입니다..
@Param("id")로 해서 query 문에서 사용되는 변수명을 적어주는거에요..
그러면 그게 해당 query문의 변수와 매핑이 됩니다..

그러면 이렇게 만든 Mapper 인터페이스를 어떻게 사용하냐면..
src/main/java/com/terry/xplatform/service/impl 패키지에 있는 SampleServiceImpl 클래스를 열어보세요..
그러면 다음과 같은 부분이 있을 겁니다..

@Autowired
private SqlSession sqlSession;
	
SampleDAO sampleDAO;
	
@PostConstruct
public void init() {
	sampleDAO = sqlSession.getMapper(SampleDAO.class);
}

이렇게 SqlSession 타입 멤버변수를 하나 설정하고
@PostConstruct 어노테이션을 통해 SampleServiceImpl이 초기화될때 sampleDAO 변수를 sqlSession.getMapper 메소드를 통해 설정해주는거에요..
여기서 SampleDAO는 위에서 언급했던 @Mapper 어노테이션이 붙은 SampleDAO 인터페이스입니다..
그 다음엔 Service에서 우리가 DAO 사용하듯 그냥 하시면 되요..
기존에 SqlSession.select 메소드나 insert 메소드를 DAO 클래스에서 일일이 작성할 필요가 없습니다..

이렇게 크게 2가지 설명한다는 내용중 하나 설명했고 또 하나는 VO 클래스의 alias 등록하는 방법이에요..
일전에 저랑 스터디 카페에서 이 부분에 대해 얘기할때 일일이 mubatis config 파일에 등록한다고 했는데..
좀더 찾아보니 스캔해서 등록해주는 기능이 있더군요..

/src/main/resources/com/terry/xplatform/resources/mybatis/config 에 가시면 mybatis-config.xml 파일이 있습니다..
그 내용도 얼마 안되니 여기다가 쓸께요..

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "HTTP://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<settings>
		<setting name="cacheEnabled" value="false" />
	</settings>
	<typeAliases>
		<package name="com.terry.xplatform.vo" />
    </typeAliases>
</configuration>

여기서 봐야 할것은 <typeAliases> 태그의 하위 태그로 있는 <package> 태그 입니다..
여기에 VO가 들어있는 패키지를 써주는거에요..그러나 패키지를 써준다고 끝나는게 아닙니다..
왜냐면 패키지 안에 있는것들중 mybatis vo로 등록할 것도 있고 그렇지 않은 것도 있기 때문에..
VO에 별도 기술해줘야 할 것이 있어요..
com.terry.xplatform.vo 패키지에 있는 SampleVO 클래스를 열어보세요..
소스도 얼마안되니 여기다가 바로 써드릴께요..

@Data
@AllArgsConstructor
@Alias("SampleVO")
public class SampleVO extends XplatformVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4891478433457788136L;

	/** 아이디 */
    private String id;
    
    /** 이름 */
    private String name;
    
    /** 내용 */
    private String description;
    
    /** 사용여부 */
    private String useYn;
    
    /** 등록자 */
    private String regUser;
}

여기서 봐야 할 부분중 @Alias가 있습니다..
@Alias 어노테이션에 mybatis 에서 사용할 VO의 이름을 써주는거에요..
그러면 위에서 <package> 태그에 설정되어 있는 패키지 안의 클래스들에서 @Alias 어노테이션이 있는 것만 Mybatis의 VO로 등록해서 사용하게 됩니다..
@Alias("SampleVO") 로 했기 때문에 query 문이 있는 sample.xml 문에서 다음과 같이 이 클래스를 사용할 수 있는거에요..

<insert id="insertSample" parameterType="SampleVo">
	<![CDATA[
		INSERT INTO SAMPLE 
			( ID
			  , NAME
			  , DESCRIPTION
			  , USE_YN
			  , REG_USER )
		VALUES ( #{id}
			  , #{name}
			  , #{description}
			  , #{useYn}
			  , #{regUser} )
	]]>
</insert>

parameterType 속성에 있는 SampleVO를 @Alias 어노테이션에 붙인 이름으로 찾아서 매핑하는거죠..

그리고 제가 보내드린 query xml 파일을 보시면 거기에 동적 sql 하는 부분이 있습니다..
그 부분이 ibatis하고는 다른 부분이 있으니까 꼭 보세요..
문서 보시고 모르는 부분은 전화주세요..
