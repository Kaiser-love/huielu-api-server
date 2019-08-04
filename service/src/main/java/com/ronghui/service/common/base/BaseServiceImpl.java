/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ronghui.service.common.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ronghui.service.common.constant.EntityConstant;
import com.ronghui.service.common.util.*;
import com.ronghui.service.dto.BladeUser;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    private Class<T> modelClass;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl() {
        Type type = this.getClass().getGenericSuperclass();
        this.modelClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[1];
    }

    @Override
    public boolean save(T entity) {
        BladeUser user = SecureUtil.getUser();
        entity.setCreateUser(user != null ? Long.valueOf(Objects.requireNonNull(user).getUserId()) : null);
        entity.setCreateTime(TimeUtil.currentTimeStamp());
        entity.setUpdateUser(user != null ? Long.valueOf(user.getUserId()) : null);
        entity.setUpdateTime(TimeUtil.currentTimeStamp());
        entity.setStatus(EntityConstant.goodProperty);
        entity.setIsDeleted(EntityConstant.badProperty);
        return super.save(entity);
    }

    @Override
    public boolean updateById(T entity) {
        BladeUser user = SecureUtil.getUser();
        entity.setUpdateUser(user != null ? Long.valueOf(Objects.requireNonNull(user).getUserId()) : null);
        entity.setUpdateTime(TimeUtil.currentTimeStamp());
        return super.updateById(entity);
    }

    @Override
    public boolean deleteLogic(@NotEmpty List<Integer> ids) {
        BladeUser user = SecureUtil.getUser();
        T entity = BeanUtil.newInstance(modelClass);
        entity.setUpdateUser(user != null ? Long.valueOf(Objects.requireNonNull(user).getUserId()) : null);
        entity.setUpdateTime(TimeUtil.currentTimeStamp());
        return super.removeByIds(ids);
    }

}
