/*
 * Copyright 2021 DataCanvas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dingodb.test.join;

import io.dingodb.test.SqlHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

public class QueryHashJoin4Test {
    private static SqlHelper sqlHelper;

    @BeforeAll
    public static void setupAll() throws Exception {
        sqlHelper = new SqlHelper();
        sqlHelper.execFile(QueryHashJoin4Test.class.getResourceAsStream("table-beauties-create.sql"));
        sqlHelper.execFile(QueryHashJoin4Test.class.getResourceAsStream("table-boys-create.sql"));
        sqlHelper.execFile(QueryHashJoin4Test.class.getResourceAsStream("table-beauties-data.sql"));
        sqlHelper.execFile(QueryHashJoin4Test.class.getResourceAsStream("table-boys-data.sql"));
    }

    @AfterAll
    public static void cleanUpAll() throws Exception {
        sqlHelper.cleanUp();
    }

    @BeforeEach
    public void setup() throws Exception {
    }

    @AfterEach
    public void cleanUp() throws Exception {
    }

    @Test
    public void testBeautiesBoysRightFilter() throws SQLException, IOException {
        sqlHelper.doQueryTest(
            this.getClass(),
            "beauties-boys-right-join-filter"
        );
    }

    @Test
    public void testBeautiesBoysRightFilterNotIn() throws SQLException, IOException {
        sqlHelper.doQueryTest(
            this.getClass(),
            "beauties-boys-right-join-filter-not-in"
        );
    }
}
