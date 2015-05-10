package com.miglab.miyo.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : 180
 * @version : 0.01
 * @email : yaobanglin@163.com
 * @created time : 2015-02-18 09:20
 * @describe : BaseEntity
 * @for your attention : none
 * @revise : none
 */
public class BaseEntity implements Serializable {

	public void initWithJsonObject(JSONObject json) {
		if(json == null)
			return;
		
		List<Field> fields = getFields();
		if (fields != null && fields.size() > 0) {
			Object obj = null;
			for (Field field : fields) {
				try {
					if (json.has(field.getName())) {
						obj = json.opt(field.getName());

						if (obj != null) {
							try {
								field.setAccessible(true);
								if (field.getType()
										.isAssignableFrom(Long.class))
									field.setLong(obj, 0);
								else
									field.set(this, obj);
							} finally {
								field.setAccessible(false);
							}
						}
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private List<Field> getFields() {
		List<Field> fieldList = new ArrayList<Field>();
		Class<?> superclass = this.getClass();
		Field[] fields = null;
		while (superclass != null && !superclass.isInterface()) {
			if (superclass.isAssignableFrom(BaseEntity.class))
				break;
			fields = superclass.getDeclaredFields();
			fieldList.addAll(Arrays.asList(fields));
			superclass = superclass.getSuperclass();
		}
		return fieldList;
	}

	public static List<? extends BaseEntity> initWithsJsonObjects(
			Class<? extends BaseEntity> entityClass, JSONArray jsonArray) {
		List<BaseEntity> entities = new ArrayList<BaseEntity>();
		try {
			for (int i = 0; i < jsonArray.length(); ++i) {
				BaseEntity baseEntity = entityClass.newInstance();
				baseEntity.initWithJsonObject(jsonArray.getJSONObject(i));
				entities.add(baseEntity);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entities;
	}

	public static BaseEntity initWithJsonObject(Class<?> entityClass,
			JSONObject json) {
		BaseEntity baseEntity = null;
		try {
			baseEntity = (BaseEntity) entityClass.newInstance();
			baseEntity.initWithJsonObject(json);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return baseEntity;
	}

	public JSONObject toJson(){
		JSONObject json = new JSONObject();

		List<Field> fields = getFields();
		if (fields != null && fields.size() > 0) {
			Object obj = null;
			for (Field field : fields) {
				try {
					String name = field.getName();
					field.setAccessible(true);  
					obj = field.get(this);
					json.put(field.getName(), obj);
					field.setAccessible(false);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		return json;
	}
}
