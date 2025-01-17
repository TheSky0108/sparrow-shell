/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.orm.type;

import com.sparrow.protocol.enums.PLATFORM;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlatformTypeHandler implements TypeHandler<PLATFORM> {
    @Override public void setParameter(PreparedStatement ps, int i, PLATFORM parameter) throws SQLException {
        ps.setInt(i, parameter.getPlatform());
    }

    @Override public PLATFORM getResult(ResultSet rs, String columnName) throws SQLException {
        return PLATFORM.getByPlatform(rs.getInt(columnName));
    }

    @Override public PLATFORM getResult(ResultSet rs, int columnIndex) throws SQLException {
        return PLATFORM.getByPlatform(rs.getInt(columnIndex));
    }

    @Override public PLATFORM getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return PLATFORM.getByPlatform(cs.getInt(columnIndex));
    }

}
