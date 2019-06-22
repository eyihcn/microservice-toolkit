package eyihcn.user.test.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import eyihcn.common.core.model.BaseEntity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
/**
 * <p>Description: </p> 
 * @author chenyi
 * @date 2019-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends BaseEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

		
		   @Override
   public Long getId() {
		return this.id;
   }
   @Override
   public void setId(Long id) {
		this.id = id;
   }
	    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	   private Long id;
   public static final String ID = "id";
	
   private String name;
   public static final String NAME = "name";
	
   private Integer age;
   public static final String AGE = "age";
	
   private String email;
   public static final String EMAIL = "email";


}