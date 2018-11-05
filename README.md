xplatform-spring
================

투비소프트의 Xplatform과 Spring Framework를 연동한 **학습용** Template 입니다

이 Template의 특징은 Controller 클래스의 메소드에서 Xplatform에서 전달한 데이터를 받을때 Xplatform에서 제공하는 클래스가 아닌 Java에서 제공하는 기본 POJO 스타일 클래스로 받을수 있다는게 특징입니다.<br/>
그래서 일반 HTML 클라이언트일때의 개발과 별 차이 없이 할 수 있습니다

이 Template에 대한 구체적인 코드 설명은 다음의 글들에 있습니다.

1. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (1) - 개요](https://zgundam.tistory.com/150)
2. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (2) - 사전지식](https://zgundam.tistory.com/151)
3. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (3) - HttpServletRequestWrapper](https://zgundam.tistory.com/152)
4. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (4) - Spring Controller에서 하려는 것](https://zgundam.tistory.com/153)
5. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (5) - HandlerMethodArgumentResolver 분석(1)](https://zgundam.tistory.com/154)
6. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (6) - HandlerMethodArgumentResolver 분석(2)](https://zgundam.tistory.com/155)
7. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (7) - HandlerMethodArgumentResolver 분석(3)](https://zgundam.tistory.com/156)
8. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (8) - HandlerMethodArgumentResolver 분석(4)](https://zgundam.tistory.com/157)
9. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (9) - XplatformView 분석](https://zgundam.tistory.com/158)
10. [Xplatform과 Spring Framework 연동 템플릿으로 보는 HandlerMethodArgumentResolver와 ViewResolver (10) - 예외처리](https://zgundam.tistory.com/159)

Xplatform 관련 소스는 **src/main/webapp/xui** 디렉토리에 있습니다.<br/>
Xplatform으로 해당 프로젝트를 열고자 할때는 xui 디렉토리에 있는 DefaultTheme.xprj 파일을 여세요<br/>
WAS를 통해 서비스를 제공할때 있어야 할 파일인 XPLATFORM\_SERVER\_License.xml 파일은 **src/main/resources**에 갖고 계신 파일을 넣어주시면 됩니다.<br/>
이 템플릿에 있는건 임시용이기 때문에 동작하지 않습니다(즉 테스트 할려면 여러분이 갖고 있는 XPLATFORM\_Server\_License.xml 파일을 넣어야 동작한다는 얘기입니다)<br/>

##주의사항

이 Template을 이용해서 개발하고 수정하는 것은 개인용이든 상업용이든 누구나 해도 됩니다.<br/>
그러나 이 템플릿은 어디까지나 **학습용으로 개발한 템플릿**이기 때문에
상용으로 사용하기엔 부족한 점이 많습니다.<br/>
그러니 실제 프로젝트에 적용할 경우 용도에 맞게 수정하세요.<br/>
그리고 템플릿에 대해 버그를 말씀해주셔도 이제는 제가 Xplatform 라이센스가 만료된 관계로 이를 수정하고 테스트를 진행할 수가 없습니다<br/>
이 템플릿에 대한 완성도를 올리고 싶어도 올릴수가 없는 상황이니 양해부탁드립니다.<br/>
질문을 주셔도 제가 테스트를 해봐서 확인을 할 수 없는 상황이니 질문에 대한 답글의 내용도 떨어질 수 있습니다..
이 점도 양해 부탁드립니다..