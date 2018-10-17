package com.terry.xplatform.config.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

import com.tobesoft.xplatform.data.ColumnHeader;
import com.tobesoft.xplatform.data.DataSet;
import com.tobesoft.xplatform.data.DataTypes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XplatformReflectionUtils {

	/**
	 * Xplatform의 DataSet에 있는 특정 컬럼의 값을 VO와 매핑하는 메소드
	 * @param field
	 * @param columnHeader
	 * @param obj
	 * @param columnValue
	 */
	public static void setField(Field field, ColumnHeader columnHeader, Object obj, String columnValue)  {

		ReflectionUtils.makeAccessible(field); 					//Field가 private 등의 접근할 수 없는 상황일때도 접근할 수 있게 한다

		Class<?> fieldType = field.getType();

		if(fieldType == String.class) {
			ReflectionUtils.setField(field, obj, columnValue);
		} else if(fieldType == char.class || fieldType == Character.class) {
			ReflectionUtils.setField(field, obj, columnValue.charAt(0));
		} else if(fieldType == short.class || fieldType == Short.class) {
			ReflectionUtils.setField(field, obj, Short.parseShort(columnValue, 10));
		} else if(fieldType == int.class || fieldType == Integer.class) {
			ReflectionUtils.setField(field, obj, Integer.parseInt(columnValue, 10));
		} else if(fieldType == long.class || fieldType == Long.class) {
			ReflectionUtils.setField(field, obj, Long.parseLong(columnValue, 10));
		} else if(fieldType == float.class || fieldType == Float.class) {
			ReflectionUtils.setField(field, obj, Float.parseFloat(columnValue));
		} else if(fieldType == double.class || fieldType == Double.class) {
			ReflectionUtils.setField(field, obj, Double.parseDouble(columnValue));
		} else if(fieldType == Date.class) {
			int dataType = columnHeader.getDataType();
			Calendar calendar = Calendar.getInstance();
			if(dataType == DataTypes.DATE) {
				if(columnValue.length() == 8) {	// yyyyMMdd
					String year = columnValue.substring(0, 4);
					String month = columnValue.substring(4, 6);
					String day = columnValue.substring(6, 8);

					calendar.set(Calendar.YEAR, Integer.parseInt(year, 10));
					calendar.set(Calendar.MONTH, Integer.parseInt(month, 10) - 1);
					calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day, 10));

				} else { // DataTypes.DATE 타입에 8자리 숫자가 들어와야 하는데 그러지 않은 것이므로 예외처리 대상이다
					throw new IllegalArgumentException("dataType must be 8 characters in Xplatform DATE type");
				}
			} else if(dataType == DataTypes.TIME) {
				if(columnValue.length() >= 6) {
					String hour = columnValue.substring(0, 2);
					String minute = columnValue.substring(2, 4);
					String second = columnValue.substring(4, 6);

					calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour, 10));
					calendar.set(Calendar.MINUTE, Integer.parseInt(minute, 10));
					calendar.set(Calendar.SECOND, Integer.parseInt(second, 10));

					if(columnValue.length() > 6) {
						String milisecond = columnValue.substring(6);
						calendar.set(Calendar.MILLISECOND, Integer.parseInt(milisecond, 10));
					}

				} else { // DataTypes.DATE 타입에 6자리 이상의 숫자가 들어와야 하는데 그러지 않은 것이므로 예외처리 대상이다
					throw new IllegalArgumentException("dataType must be 6 characters in Xplatform TIME type");
				}
			} else if(dataType == DataTypes.DATE_TIME) {
				if(columnValue.length() >= 14) {
					String year = columnValue.substring(0, 4);
					String month = columnValue.substring(4, 6);
					String day = columnValue.substring(6, 8);
					String hour = columnValue.substring(8, 10);
					String minute = columnValue.substring(10, 12);
					String second = columnValue.substring(12, 14);

					calendar.set(Calendar.YEAR, Integer.parseInt(year, 10));
					calendar.set(Calendar.MONTH, Integer.parseInt(month, 10) - 1);
					calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day, 10));
					calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour, 10));
					calendar.set(Calendar.MINUTE, Integer.parseInt(minute, 10));
					calendar.set(Calendar.SECOND, Integer.parseInt(second, 10));

					if(columnValue.length() > 14) {
						String milisecond = columnValue.substring(14);
						calendar.set(Calendar.MILLISECOND, Integer.parseInt(milisecond, 10));
					}

				} else { // DataTypes.DATE 타입에 14자리 이상의 숫자가 들어와야 하는데 그러지 않은 것이므로 예외처리 대상이다
					throw new IllegalArgumentException("dataType must be 14 charactersin Xplatform DATE_TIME type");
				}
			}

			ReflectionUtils.setField(field, obj, calendar.getTime());
		}

	}

	/*
	public static <T> Collection<T> getCollection(Class clazz) {
		Collection<T> result = null;
		if(clazz == ArrayList.class) {
			result = new ArrayList<T>();
		} else if(clazz == HashSet.class) {
			result = new HashSet<T>();
		}

		return result;
	}
	*/

	/**
	 * Java Reflection에서 Field 객체를 가져올때 해당 객체의 Super 클래스들에 대한 Field를 같이 가져오는 것이 없어서 별도로 제작했다
	 * @param t
	 * @return 해당 객체가 가지고 있는 Field 객체들 및 해당 객체의 Super 클래스들이 가지고 있는 Field 객체들이 들어있는 List 객체
	 */
	public static <T> List<Field> getFields(T t) {
		List<Field> fields = new ArrayList<>();
        Class<?> clazz = t.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
	}

	/**
	 * DataSet 클래스 객체를 Collection 인터페이스 계열의 클래스 객체로 변환하는 메소드
	 * @param dataSet					변환대상이 되는 DataSet
	 * @param collectionType			변환되는 Collection 계열 인터페이스 및 클래스 타입
	 * @param genericClass				변환되는 Collection 계열 인터페이스 및 클래스의 내부에 들어사는 클래스 타입
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Object convertDataSetToCollection(DataSet dataSet, Class<?> collectionType, Class<?> genericClass) throws InstantiationException, IllegalAccessException {
		Object result = null;
		int insertUpdateRowCount = dataSet.getRowCount();
		int removeRowCount = dataSet.getRemovedRowCount();
		int columnCount = dataSet.getColumnCount(); // 데이터셋에서 삭제된 데이터행(DataRow)는 별도 공간에 저장되기 때문에 이에 대한 처리를 위해 별도 변수를 잡고 이에 대한 작업을 진행한다

		if(collectionType.isInterface()) {													// Colleciton 계열 인터페이스로 선언한 경우
			if(collectionType.equals(List.class)) {											// List 타입으로 받을 경우(ArrayList를 생성해서 return 해준다)
    			List<Object> listResult = new ArrayList<Object>();

    			// 저장(insert, update)되어야 할 DataSet의 row들에 대한 처리
    			for(int i=0; i < insertUpdateRowCount; i++) {
    				if(Map.class.isAssignableFrom(genericClass)) {
    					Map<String, Object> obj = makeDataAsMap(genericClass, dataSet, columnCount, i);
    					listResult.add(obj);
    				} else {
    					Object obj = makeDataAsObject(genericClass, dataSet, columnCount, i);
    					listResult.add(obj);
    				}
    			}

    			// 삭제(delete)되어야 할 DataSet의 row들에 대한 처리
    			for(int i = 0; i < removeRowCount; i++) {
    				if(Map.class.isAssignableFrom(genericClass)) {
    					Map<String, Object> obj = makeRemovedDataAsMap(genericClass, dataSet, columnCount, i);
    					listResult.add(obj);
    				} else {
    					Object obj = makeRemovedDataAsObject(genericClass, dataSet, columnCount, i);
    					listResult.add(obj);
    				}
    			}

    			result = listResult;
    		} else if(collectionType.equals(Set.class)) {								// Set 타입으로 받을 경우(HashSet을 생성해서 return 해준다)
    			Set<Object> setResult = new HashSet<Object>();

    			// 저장(insert, update)되어야 할 DataSet의 row들에 대한 처리
    			for(int i=0; i < insertUpdateRowCount; i++) {
    				if(Map.class.isAssignableFrom(genericClass)) {
    					Map<String, Object> obj = makeDataAsMap(genericClass, dataSet, columnCount, i);
    					setResult.add(obj);
    				} else {
    					Object obj = makeDataAsObject(genericClass, dataSet, columnCount, i);
    					setResult.add(obj);
    				}
    			}

    			// 삭제(delete)되어야 할 DataSet의 row들에 대한 처리
    			for(int i = 0; i < removeRowCount; i++) {
    				if(Map.class.isAssignableFrom(genericClass)) {
    					Map<String, Object> obj = makeRemovedDataAsMap(genericClass, dataSet, columnCount, i);
    					setResult.add(obj);
    				} else {
    					Object obj = makeRemovedDataAsObject(genericClass, dataSet, columnCount, i);
    					setResult.add(obj);
    				}
    			}

    			result = setResult;
    		}
		} else {																		// List, Set 같은 인터페이스가 아니라 ArrayList, HashSet 같은 Collection 인터페이스 구현 클래스로 받은 경우
			Object checkObject = collectionType.newInstance();
			Class<?> checkType = checkObject.getClass();
			if(Collection.class.isAssignableFrom(checkType)) {

    			// 저장(insert, update)되어야 할 DataSet의 row들에 대한 처리
    			for(int i=0; i < insertUpdateRowCount; i++) {
    				if(Map.class.isAssignableFrom(genericClass)) {
    					Map<String, Object> obj = makeDataAsMap(genericClass, dataSet, columnCount, i);
    					((Collection)(checkObject)).add(obj);
    				} else {
    					Object obj = makeDataAsObject(genericClass, dataSet, columnCount, i);
    					((Collection)(checkObject)).add(obj);
    				}
    			}

    			// 삭제(delete)되어야 할 DataSet의 row들에 대한 처리
    			for(int i = 0; i < removeRowCount; i++) {
    				if(Map.class.isAssignableFrom(genericClass)) {
    					Map<String, Object> obj = makeRemovedDataAsMap(genericClass, dataSet, columnCount, i);
    					((Collection)(checkObject)).add(obj);
    				} else {
    					Object obj = makeRemovedDataAsObject(genericClass, dataSet, columnCount, i);
    					((Collection)(checkObject)).add(obj);
    				}
    			}
			}

			result = checkObject;
		}



		return result;
	}

	/**
     * DataSet 객체에서 특정 row에 해당되는 객체를 Map<String, Object> 형태로 변환한다
     * 변환된 Map에서 key는 해당 DataSet의 column 이름이다.
     * @param dataSet		DataSet 객체
     * @param colCount		DataSet 객체의 컬럼 갯수
     * @param rowIdx		DataSet 객체에서 읽고자 하는 row의 인덱스
     * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
     */
    private static Map<String, Object> makeDataAsMap(Class<?> mapType, DataSet dataSet, int colCount, int rowIdx) throws InstantiationException, IllegalAccessException {
    	Map<String, Object> result = null;
    	if(mapType.isInterface()) {
    		result = new HashMap<String, Object>();
    	} else {
    		result = (Map<String, Object>) mapType.newInstance();
    	}

    	result.put("rowType", dataSet.getRowType(rowIdx));
		result.put("storeDataChanges", dataSet.isStoreDataChanges());
		for(int j=0; j < colCount; j++) {
			Object columnValue = dataSet.getObject(rowIdx, j);				// 데이터셋에서 해당 row에 대한 column 값을 Object로 가져온다

			ColumnHeader columnHeader = dataSet.getColumn(j);
			String columnName = columnHeader.getName();
			result.put(columnName, columnValue);
		}

		return result;
    }

    /**
     * DataSet 객체에 저장되어 있는 삭제된 데이터에서 특정 row에 해당되는 객체를 Map<String, Object> 형태로 변환한다
     * 변환된 Map에서 key는 해당 DataSet의 column 이름이다.
     * @param dataSet		DataSet 객체
     * @param colCount		DataSet 객체의 컬럼 갯수
     * @param rowIdx		DataSet 객체에서 읽고자 하는 row의 인덱스
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static Map<String, Object> makeRemovedDataAsMap(Class<?> mapType, DataSet dataSet, int colCount, int rowIdx) throws InstantiationException, IllegalAccessException {
    	Map<String, Object> result = null;
    	if(mapType.isInterface()) {
    		result = new HashMap<String, Object>();
    	} else {
    		result = (Map<String, Object>) mapType.newInstance();
    	}

		result.put("rowType", DataSet.ROW_TYPE_DELETED);
		result.put("storeDataChanges", dataSet.isStoreDataChanges());
		for(int j=0; j < colCount; j++) {
			Object columnValue = dataSet.getRemovedData(rowIdx, j);				// 데이터셋에서 해당 row에 대한 column 값을 Object로 가져온다

			ColumnHeader columnHeader = dataSet.getColumn(j);
			String columnName = columnHeader.getName();
			result.put(columnName, columnValue);
		}

		return result;
    }

    /**
     * DataSet 객체에서 특정 row에 해당되는 객체를 특정 클래스의 객체로 변환한다
     * @param innerClass				특정 클래스타입
     * @param dataSet					DataSet 객체
     * @param colCount					DataSet 객체의 컬럼 갯수
     * @param rowIdx					DataSet 객체에서 읽고자 하는 row의 인덱스
     * @return
     * @throws InstantiationException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static Object makeDataAsObject(Class<?> innerClass, DataSet dataSet, int colCount, int rowIdx) throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    	Object obj = innerClass.newInstance();
		Field rowTypeField = ReflectionUtils.findField(innerClass, "rowType");
		Field storeDataChangesField = ReflectionUtils.findField(innerClass, "storeDataChanges");
		ReflectionUtils.makeAccessible(rowTypeField);
		ReflectionUtils.makeAccessible(storeDataChangesField);
		rowTypeField.setInt(obj, dataSet.getRowType(rowIdx));
		storeDataChangesField.setBoolean(obj, dataSet.isStoreDataChanges());

		for(int j=0; j < colCount; j++) {
			String columnValue = dataSet.getString(rowIdx, j);				// 데이터셋에서 해당 row에 대한 column 값을 String으로 가져온다

			ColumnHeader columnHeader = dataSet.getColumn(j);
			String columnName = columnHeader.getName();

			Field field = ReflectionUtils.findField(innerClass, columnName);

			if(field == null) {		// 컬럼이름으로 된 멤버변수를 찾지 못한 경우
				continue;
			} else {
				XplatformReflectionUtils.setField(field, columnHeader, obj, columnValue);
			}
		}

		return obj;
    }

    /**
     * DataSet 객체에서 특정 row에 해당되는 객체를 특정 클래스의 객체로 변환한다
     * @param innerClass				특정 클래스타입
     * @param dataSet					DataSet 객체
     * @param colCount					DataSet 객체의 컬럼 갯수
     * @param rowIdx					DataSet 객체에서 읽고자 하는 row의 인덱스
     * @return
     * @throws InstantiationException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static Object makeRemovedDataAsObject(Class<?> innerClass, DataSet dataSet, int colCount, int rowIdx) throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    	Object obj = innerClass.newInstance();
		Field rowTypeField = ReflectionUtils.findField(innerClass, "rowType");
		Field storeDataChangesField = ReflectionUtils.findField(innerClass, "storeDataChanges");
		ReflectionUtils.makeAccessible(rowTypeField);
		ReflectionUtils.makeAccessible(storeDataChangesField);

		// 삭제된 행의 rowType을 읽어오는 메소드는 없기 때문에(DataSet 클래스의 getRowType 메소드는 저장이나 변경된 데이터셋에 대한 rowType 을 읽어오는 것이지 삭제된 행에 대한 rowType을 읽는 것이 아니다)
		// 이렇게 된 원인은 삭제된 행을 별도로 저장하는 상황때문에 그리 된거지만 어차피 삭제된 행은 rowType이 DataSet.ROW_TYPE_DELETED로 설정되기 때문에 이것으로 강제 설정을 해준다
		rowTypeField.setInt(obj, DataSet.ROW_TYPE_DELETED);
		storeDataChangesField.setBoolean(obj, dataSet.isStoreDataChanges());

		for(int j=0; j < colCount; j++) {
			String columnValue = dataSet.getRemovedStringData(rowIdx, j);		// 삭제된 데이터의 해당 row에 대한 column 값을 String으로 가져온다

			ColumnHeader columnHeader = dataSet.getColumn(j);
			String columnName = columnHeader.getName();

			Field field = ReflectionUtils.findField(innerClass, columnName);

			if(field == null) {		// 컬럼이름으로 된 멤버변수를 찾지 못한 경우
				continue;
			} else {
				XplatformReflectionUtils.setField(field, columnHeader, obj, columnValue);
			}
		}

		return obj;
    }
}
