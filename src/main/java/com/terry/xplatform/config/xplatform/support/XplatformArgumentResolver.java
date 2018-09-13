package com.terry.xplatform.config.xplatform.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.terry.xplatform.config.utils.XplatformReflectionUtils;
import com.terry.xplatform.config.xplatform.annotation.RequestDataSet;
import com.terry.xplatform.config.xplatform.annotation.RequestDataSetList;
import com.terry.xplatform.config.xplatform.annotation.RequestVariable;
import com.tobesoft.xplatform.data.DataSet;
import com.tobesoft.xplatform.data.DataSetList;
import com.tobesoft.xplatform.data.PlatformData;
import com.tobesoft.xplatform.data.Variable;
import com.tobesoft.xplatform.data.VariableList;
import com.tobesoft.xplatform.tx.HttpPlatformRequest;
import com.tobesoft.xplatform.tx.PlatformRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XplatformArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * DataSetList 객체를 HttpServletRequest의 setAttribute 메소드를 이용해서 저장할 때 사용되는 이름
	 */
	private final String DATASETLIST_NAME;

	/**
	 * VariableList 객체를 HttpServletRequest의 setAttribute 메소드를 이용해서 저장할 때 사용되는 이름
	 */
	private final String VARIABLELIST_NAME;

	public XplatformArgumentResolver() {
		this.DATASETLIST_NAME = "dataSetList";
		this.VARIABLELIST_NAME = "variableList";
	}

	public XplatformArgumentResolver(String dataSetListName, String variableListName) {
		this.DATASETLIST_NAME = dataSetListName;
		this.VARIABLELIST_NAME = variableListName;
	}

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
		// TODO Auto-generated method stub
		boolean result = false;
		if(parameter.hasParameterAnnotation(RequestDataSetList.class)){
		    result = true;
		}else if(parameter.hasParameterAnnotation(RequestDataSet.class)){
		    result = true;
		}else if(parameter.hasParameterAnnotation(RequestVariable.class)){
		    result = true;
		}

		return result;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    	// TODO Auto-generated method stub

    	Class<?> type = parameter.getParameterType();
    	Annotation[] annotations = parameter.getParameterAnnotations();
    	HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
    	// HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    	// PlatformRequest platformRequest = new HttpPlatformRequest(request, "utf-8");

    	DataSetList dataSetList = null;
    	VariableList variableList = null;

    	// 관련 처리를 할때마다 매번 Request의 Body에 있는 DataSetList와 VariableList를 만들면 오버헤드 소지가 있어서
    	// 처음에 한번만 읽고  DataSetList와 VariableList를 HttpServletRequest에 setAttribute를 통해 저장을 한 뒤에
    	// 다시 읽을때는 Request의 Body를 읽는게 아니라 저장되어 있는 것을 다시 읽어서 재활용한다
    	if(request.getAttribute(DATASETLIST_NAME) == null || request.getAttribute(VARIABLELIST_NAME) == null) {
    		PlatformRequest platformRequest = new HttpPlatformRequest(request.getInputStream());
        	platformRequest.receiveData();
        	PlatformData platformData = platformRequest.getData();
        	dataSetList = platformData.getDataSetList();
        	variableList = platformData.getVariableList();

        	request.setAttribute(DATASETLIST_NAME, dataSetList);
        	request.setAttribute(VARIABLELIST_NAME, variableList);
    	} else {
    		dataSetList = (DataSetList)request.getAttribute(DATASETLIST_NAME);
    		variableList = (VariableList)request.getAttribute(VARIABLELIST_NAME);
    	}

    	Object result = null;


		for(Annotation annotation : annotations){
		    Class<? extends Annotation> annotationClass = annotation.annotationType();

		    if(annotationClass.equals(RequestDataSetList.class)){			// @RequestDataSetList 어노테이션에 대한 처리(이 어노테이션은 DataSetList 클래스객체만 파라미터 타입으로 받을 수 있다)
		    	if(type.equals(DataSetList.class)) {
		    		result = dataSetList;
		    	}else{
		    		result = WebArgumentResolver.UNRESOLVED;
		    	}
		    } else if(annotationClass.equals(RequestDataSet.class)){		// @RequestDataSet 어노테이션에 대한 처리
		    	RequestDataSet requestDataSet = (RequestDataSet)annotation;
		    	String dataSetName = requestDataSet.name();
		    	if(!StringUtils.hasText(dataSetName)) {						// DataSet 이름이 빠진것이므로 이거는 예외처리 진행하자
		    		result = WebArgumentResolver.UNRESOLVED;
		    	} else {
		    		DataSet dataSet = dataSetList.get(dataSetName);
		    		if(type.equals(DataSet.class)) {							// DataSet 파라미터 타입으로 받을 경우는 해당 이름으로 DataSet을 찾아서 이를 return 해주면 된다
		    			result = dataSet;
		    		} else {													// DataSet 클래스 객체는 Collection 인터페이스 계열 클래스들만 변환이 가능하기 때문에 이에 대한 체크
		    			if(Collection.class.isAssignableFrom(type)) {
		    				// Type[] typeArray = ((ParameterizedType)parameter.getGenericParameterType()).getActualTypeArguments();
		    				// Class<?> genericClassType = (Class<?>) ((ParameterizedType)parameter.getGenericParameterType()).getActualTypeArguments()[0];

		    				Type genericType = ((ParameterizedType)parameter.getGenericParameterType()).getActualTypeArguments()[0];
		    				Class<?> genericClass = null;
		    				if(genericType instanceof Class) {
		    					genericClass = (Class<?>)genericType;
		    				} else {
		    					Type rawType = ((ParameterizedType)genericType).getRawType();
		    					genericClass = (Class<?>)rawType;
		    				}

		    				result = XplatformReflectionUtils.convertDataSetToCollection(dataSet, type, genericClass);
		    			} else {
		    				result = WebArgumentResolver.UNRESOLVED;
		    			}
		    		}
		    	}

		    	// XplatformReflectionUtils.convertDataSetToCollection 메소드로 변환할 수 없는 타입일 경우 null이 return 되기 때문에 이에 대한 처리
		    	if(result == null) {
		    		result = WebArgumentResolver.UNRESOLVED;
		    	}
		    } else if(annotationClass.equals(RequestVariable.class)) {	// @RequestVariable 어노테이션에 대한 처리
		    	RequestVariable requestVariable = (RequestVariable)annotation;
		    	String variableName = requestVariable.name();
		    	Variable variable = variableList.get(variableName);
		    	if(StringUtils.hasText(variableName)) {					// 특정 변수 이름이 있기 때문에 해당 이름에 대한 값을 낸다
		    		if(type.equals(int.class)) {
		    			result = variable.getInt();
		    		} else if(type.equals(Integer.class)) {
		    			result = new Integer(variable.getInt());
		    		} else if(type.equals(long.class)) {
		    			result = variable.getLong();
		    		} else if(type.equals(Long.class)) {
		    			result = new Long(variable.getLong());
		    		} else if(type.equals(float.class)) {
		    			result = variable.getFloat();
		    		} else if(type.equals(Float.class)) {
		    			result = new Float(variable.getFloat());
		    		} else if(type.equals(double.class)) {
		    			result = variable.getDouble();
		    		} else if(type.equals(Double.class)) {
		    			result = new Double(variable.getDouble());
		    		} else if(type.equals(Date.class)) {
		    			result = variable.getDateTime();
		    		} else if(type.equals(String.class)) {
		    			result = variable.getString();
		    		} else {
		    			result = variable.getObject();
		    		}
		    	} else {												// 특정 변수 이름이 없으면 VO로 매핑하는 것이기 때문에 오히려 이런 경우 자바의 데이터타입과는 매핑을 할 수 없다
		    		List<String> keyList = variableList.keyList();

		    		if(Collection.class.isAssignableFrom(type)) {
		    			Collection collectionResult = null;
		    			if(type.isInterface()) {
		    				if(type == List.class) {
	    						collectionResult = new ArrayList<Object>();
	    					} else if(type == Set.class) {
	    						collectionResult = new HashSet<Object>();
	    					}
		    			} else {
		    				collectionResult = (Collection)type.newInstance();
		    			}

		    			for(String key : keyList) {
	    					collectionResult.add(variableList.getObject(key));
	    				}

	    				result = collectionResult;
		    		} else if(Map.class.isAssignableFrom(type)) {
		    			Map<String, Object> mapResult = null;
	    				if(type.isInterface()) {
	    					mapResult = new HashMap<String, Object>();
	    				} else {
	    					mapResult = (Map<String, Object>)type.newInstance();
	    				}

	    				for(String key : keyList) {
	    					mapResult.put(key, variableList.getObject(key));
	    				}

	    				result = mapResult;
		    		} else {
		    			// 객체로 변환하는 과정에서 예외가 발생하면 지원하지 않는 타입이기 때문에 그에 대한 처리를 한다
		    			try {

			    			Object obj = type.newInstance();

		    				for(String key : keyList) {
		    					Field keyField = ReflectionUtils.findField(type, key);
		    					ReflectionUtils.makeAccessible(keyField);

		    					Class<?> keyFieldType = keyField.getType();

		    					if(keyFieldType == int.class) {
		    						keyField.setInt(obj, variableList.getInt(key));
		    					} else if(keyFieldType == Integer.class) {
		    						keyField.set(obj, new Integer(variableList.getInt(key)));
		    					} else if(keyFieldType == long.class) {
		    						keyField.setLong(obj, variableList.getLong(key));
		    					} else if(keyFieldType == Long.class) {
		    						keyField.set(obj, new Long(variableList.getLong(key)));
		    					} else if(keyFieldType == float.class) {
		    						keyField.setFloat(obj, variableList.getFloat(key));
		    					} else if(keyFieldType == Float.class) {
		    						keyField.set(obj, new Float(variableList.getFloat(key)));
		    					} else if(keyFieldType == double.class) {
		    						keyField.setDouble(obj, variableList.getDouble(key));
		    					} else if(keyFieldType == Double.class) {
		    						keyField.set(obj, new Double(variableList.getFloat(key)));
		    					} else if(keyFieldType == Date.class) {
		    						keyField.set(obj, variableList.getDateTime(key));
		    					} else if(keyFieldType == String.class) {
		    						keyField.set(obj, variableList.getString(key));
		    					} else {
		    						keyField.set(obj, variableList.getObject(key));
		    					}
		    				}

		    				result = obj;
		    			} catch(Exception e) {
		    				result = WebArgumentResolver.UNRESOLVED;
		    			}
		    		}
		    	}
		    }

		}

		return result;
    }
}
