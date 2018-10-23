package com.terry.xplatform.config.xplatform.web;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContext;

import com.terry.xplatform.config.utils.XplatformReflectionUtils;
import com.terry.xplatform.config.xplatform.XplatformConstants;
import com.tobesoft.xplatform.data.DataSet;
import com.tobesoft.xplatform.data.DataSetList;
import com.tobesoft.xplatform.data.DataTypes;
import com.tobesoft.xplatform.data.PlatformData;
import com.tobesoft.xplatform.data.Variable;
import com.tobesoft.xplatform.data.VariableList;
import com.tobesoft.xplatform.data.datatype.PlatformDataType;
import com.tobesoft.xplatform.tx.HttpPlatformResponse;

public class XplatformView implements View {

	/**
	 * Xplatform의 작업결과가 성공적이었을때의 ErrorCode 값을 설정한다
	 */
	private final String ERROR_CODE_VALUE;

	/**
	 * Xplatform의 작업결과가 성공적이었을때의 ErrorMsg 값을 설정한다
	 */
	private final String ERROR_MSG_VALUE;

	public XplatformView() {
		this.ERROR_CODE_VALUE = "0";
		this.ERROR_MSG_VALUE = "";
	}

	public XplatformView(String errorCodeValue, String errorMsgValue) {
		this.ERROR_CODE_VALUE = errorCodeValue;
		this.ERROR_MSG_VALUE = errorMsgValue;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		String contentType = request.getHeader("Content-Type").startsWith("text/xml;") ? XplatformConstants.CONTENT_TYPE_XML
				: request.getHeader("Content-Type");

    	if(contentType == XplatformConstants.CONTENT_TYPE_XML) {
    		VariableList variableList = new VariableList();
    		DataSetList dataSetList = new DataSetList();
    		HttpPlatformResponse httpPlatformResponse = new HttpPlatformResponse(response, XplatformConstants.CONTENT_TYPE_XML);

    		if(model != null) {

	    		for(Entry<String, ?> entry : model.entrySet()) {
	    			String key = entry.getKey();
	    			Object object = entry.getValue();
	    			if(object instanceof Collection) {
	    				@SuppressWarnings("unchecked")
						DataSet dataSet = makeDataSet(key, (Collection<Object>)object);
	    				dataSetList.add(dataSet);
	    			} else {
	    				Variable variable = null;
	    				if(object instanceof Integer) {
	    					variable = new Variable(key, PlatformDataType.INT, (Integer)object);
	    				} else if(object instanceof Long) {
	    					variable = new Variable(key, PlatformDataType.LONG, (Long)object);
	    				} else if(object instanceof Float) {
	    					variable = new Variable(key, PlatformDataType.FLOAT, (Float)object);
	    				} else if(object instanceof Double) {
	    					variable = new Variable(key, PlatformDataType.DOUBLE, (Double)object);
	    				} else if(object instanceof Date) {
	    					variable = new Variable(key, PlatformDataType.DATE, (Date)object);
	    				} else if(object instanceof String){
	    					variable = new Variable(key, PlatformDataType.STRING, (String)object);
	    				} else if(object instanceof Variable) {
	    					variable = (Variable)object;
	    				} else {
	    					// model에 들어있는 클래스 객체중에 DataSet으로 변환할 수 없는 클래스 객체가 들어있는것은 bypass 하게끔 한다

	    					if(skipDataSet(object)) {
	    						continue;
	    					}

	    					// 객체의 멤버변수들 값을 읽어서 한 행짜리 데이터셋으로 return 하는 방법을 고민해보자
	    					DataSet dataSet = makeDataSet(key, object);
	        				dataSetList.add(dataSet);
	    				}
	    				if(variable != null) {
	    					variableList.add(variable);
	    				}
	    			}
	    		}

	    		// XplatformView를 만든다는 것은 그 이전단계까지는 예외없이 진행되었다는 뜻이기 때문에 Xplatform에서 읽어들일변수인 ErrorCode 와 ErrorMsg 변수에 작업이 성공했다는 내용을 설정한다
	    		// Controller에서 ErrorCode와 ErrorMsg를 설정한 것이 없으면 XplatformView에서 설정하도록 한다
	    		if(!model.containsKey("ErrorCode")) {
	    			variableList.add("ErrorCode", ERROR_CODE_VALUE);
	    		}

	    		if(!model.containsKey("ErrorMsg")) {
	    			variableList.add("ErrorMsg", ERROR_MSG_VALUE);
	    		}
    		}

    		PlatformData platformData = new PlatformData();
		    platformData.setVariableList(variableList);
		    platformData.setDataSetList(dataSetList);
		    httpPlatformResponse.setData(platformData);
		    httpPlatformResponse.sendData();

    	} else if(contentType == XplatformConstants.CONTENT_TYPE_CSV) {

    	}
	}

	/**
     * Collection 객체와 DataSet 이름을 파라미터로 받아 해당 객체가 들어있는 DataSet을 return 한다
     * @param dataSetName
     * @param collection
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private DataSet makeDataSet(String dataSetName, Collection<Object> collection) throws IllegalArgumentException, IllegalAccessException {
    	DataSet dataSet = new DataSet(dataSetName);
    	Object objColumn = null;
    	if(collection != null && collection.size() != 0) {
    		objColumn = collection.iterator().next();
    		setColumnHeader(dataSet, objColumn);
    		addCollectionToDataSet(dataSet, collection);
    	}
    	return dataSet;
    }

    /**
     * Object 객체와 DataSet 이름을 파라미터로 받아 해당 객체가 들어있는 DataSet을 return 한다
     * @param dataSetName
     * @param object
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private DataSet makeDataSet(String dataSetName, Object object) throws IllegalArgumentException, IllegalAccessException {
    	DataSet dataSet = new DataSet(dataSetName);
    	setColumnHeader(dataSet, object);
    	addObjectToDataSet(dataSet, object);
    	return dataSet;
    }

    /**
     * DataSet 객체와 DataSet에 들어갈 객체를 파라미터로 받아 DataSet 객체에 ColumnHeader 정보를 설정한다
     * @param dataSet
     * @param object
     */
    private void setColumnHeader(DataSet dataSet, Object object) {
    	if(object instanceof Map) {
    		@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) object;
    		for(Map.Entry<String, Object> entry : map.entrySet()) {
    			Object value = entry.getValue();

    			if(value instanceof Integer) {
    				dataSet.addColumn(entry.getKey(), DataTypes.INT);
    			} else if(value instanceof Long) {
    				dataSet.addColumn(entry.getKey(), DataTypes.LONG);
    			} else if(value instanceof Float) {
    				dataSet.addColumn(entry.getKey(), DataTypes.FLOAT);
    			} else if(value instanceof Double) {
    				dataSet.addColumn(entry.getKey(), DataTypes.DOUBLE);
    			} else if(value instanceof Date) {
    				dataSet.addColumn(entry.getKey(), DataTypes.DATE);
    			} else {
    				dataSet.addColumn(entry.getKey(), DataTypes.STRING);
    			}
    		}
    	} else {
    		List<Field> fieldList = XplatformReflectionUtils.getFields(object);
    		for(Field field : fieldList) {
    			field.setAccessible(true);
    			String fieldName = field.getName();
    			Class<?> classType = field.getType();
    			if(classType == int.class || classType == Integer.class) {
    				dataSet.addColumn(fieldName, DataTypes.INT);
    			} else if(classType == long.class || classType == Long.class) {
    				dataSet.addColumn(fieldName, DataTypes.LONG);
    			} else if(classType == float.class || classType == Float.class) {
    				dataSet.addColumn(fieldName, DataTypes.FLOAT);
    			} else if(classType == double.class || classType == Double.class) {
    				dataSet.addColumn(fieldName, DataTypes.DOUBLE);
    			} else if(classType == Date.class) {
    				dataSet.addColumn(fieldName, DataTypes.DATE);
    			} else {
    				dataSet.addColumn(fieldName, DataTypes.STRING);
    			}
    		}
    	}
    }

    /**
     * Collection 객체를 받아 Collection 객체 안에 있는 객체들을 묶어서 하나의 DataSet으로 만들어서 추가한다
     * @param dataSet
     * @param collection
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void addCollectionToDataSet(DataSet dataSet, Collection<Object> collection) throws IllegalArgumentException, IllegalAccessException {
    	Iterator<Object> iterator = collection.iterator();
    	// Object value = collection.iterator().next();

    	while(iterator.hasNext()) {
    		Object value = iterator.next();
    		addObjectToDataSet(dataSet, value);
    	}
    }

    /**
     * Object 객체를 받아 DataSet에 추가한다
     * @param dataSet						대상이 되는 DataSet
     * @param object						추가되는 Object 객체
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void addObjectToDataSet(DataSet dataSet, Object object) throws IllegalArgumentException, IllegalAccessException {
    	int rowIdx = dataSet.newRow();

    	if(object instanceof Map) {
    		@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>)object;
			for(Map.Entry<String, Object> entry : map.entrySet()) {
				String columnName = entry.getKey();
    			Object mapValue = entry.getValue();

    			if(mapValue instanceof Integer) {
    				dataSet.set(rowIdx, columnName, (int)mapValue);
    			} else if(mapValue instanceof Long) {
    				dataSet.set(rowIdx, columnName, (long)mapValue);
    			} else if(mapValue instanceof Float) {
    				dataSet.set(rowIdx, columnName, (float)mapValue);
    			} else if(mapValue instanceof Double) {
    				dataSet.set(rowIdx, columnName, (double)mapValue);
    			} else if(mapValue instanceof Date) {
    				dataSet.set(rowIdx, columnName, (Date)mapValue);
    			} else if(mapValue instanceof Boolean) {									// boolean 계열은 String의 valueOf 메소드로 값을 설정한다
    				dataSet.set(rowIdx, columnName, String.valueOf((Boolean)mapValue));
    			} else {
    				dataSet.set(rowIdx, columnName, (String)mapValue);
    			}
			}
    	} else {
    		List<Field> fieldList = XplatformReflectionUtils.getFields(object);

			for(Field field : fieldList) {
				field.setAccessible(true);
				String columnName = field.getName();
				Class<?> classType = field.getType();

				if(classType == int.class ) {
					dataSet.set(rowIdx, columnName, field.getInt(object));
				} else if(classType == Integer.class) {
					dataSet.set(rowIdx, columnName, field.get(object) == null ? null : ((Integer)field.get(object)).intValue());
				} else if(classType == long.class) {
					dataSet.set(rowIdx, columnName, field.getLong(object));
				} else if(classType == Long.class) {
					dataSet.set(rowIdx, columnName, field.get(object) == null ? null : ((Long)field.get(object)).longValue());
				} else if(classType == float.class) {
					dataSet.set(rowIdx, columnName, field.getFloat(object));
				} else if(classType == Float.class) {
					dataSet.set(rowIdx, columnName, field.get(object) == null ? null : ((Float)field.get(object)).floatValue());
				} else if(classType == double.class) {
					dataSet.set(rowIdx, columnName, field.getDouble(object));
				} else if(classType == Double.class) {
					dataSet.set(rowIdx, columnName, field.get(object) == null ? null : ((Double)field.get(object)).doubleValue());
				} else if(classType == Date.class) {
					dataSet.set(rowIdx, columnName, (Date)(field.get(object)));
				} else if(classType == boolean.class || classType == Boolean.class) {		// boolean 계열은 String의 valueOf 메소드로 값을 설정한다
					dataSet.set(rowIdx, columnName, String.valueOf((field.get(object))));
				} else {
					dataSet.set(rowIdx, columnName, (String)(field.get(object)));
				}
			}
    	}
    }

    /**
     * renderMergedTemplateModel 메소드의 model 파라미터에 있는 객체들 중에 DataSet으로 표현할 수 없는 클래스들이 있다(주로 Spring이 자체적으로 넣어놓은 property bind 등의 클래스)
     * model에 있는 객체중 이런 클래스 객체는 DataSet으로의 변환을 제외시킬 목적으로 만든 메소드이며 개발하는 도중에 이런 성격의 클래스가 있으면 classArray 배열에 클래스를  객체에 있는 것중ㅇ
     * @param object
     * @return
     */
    private boolean skipDataSet(Object object) {
    	Class<?> [] classArray = new Class<?>[] {BeanPropertyBindingResult.class, RequestContext.class};

    	boolean result = false;
    	Class<?> objectClass = object.getClass();

    	for(Class<?> clazz : classArray) {
    		if(clazz == objectClass) {
    			result = true;
    			break;
    		}
    	}

    	return result;
    }

}
