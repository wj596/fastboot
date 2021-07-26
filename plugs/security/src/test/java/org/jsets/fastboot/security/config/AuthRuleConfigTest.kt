package org.jsets.fastboot.security.config

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class AuthRuleConfigTest {

    @Test
    fun case1(){
        assertThrows(IllegalStateException::class.java) {
            val c = AuthRuleConfig()
            c.parseFromLine("-->none")
        }

        assertThrows(IllegalStateException::class.java) {
            val c = AuthRuleConfig()
            c.parseFromLine("/**-->")
        }

        assertThrows(IllegalStateException::class.java) {
            val c = AuthRuleConfig()
            c.parseFromLine("/logout/**logout")
        }

        assertThrows(IllegalStateException::class.java) {
            val c = AuthRuleConfig()
            c.parseFromLine("/logout/**-->  ")
        }

        assertThrows(IllegalStateException::class.java) {
            val c = AuthRuleConfig()
            c.parseFromLine("    -->logout")
        }

        assertThrows(IllegalStateException::class.java) {
            val c3 = AuthRuleConfig()
            c3.parseFromLine("/**-->[admin,test]")
        }
    }

    @Test
    fun case2(){
        Assertions.assertTrue {
            val c = AuthRuleConfig()
            c.parseFromLine("  /**  -->roles[admin,test]")
            "/**" == c.accessPath;
        }

        Assertions.assertTrue {
            val c = AuthRuleConfig()
            c.parseFromLine("  /**  -->  roles[admin,test]  ,token")
            println(c)
            "/**" == c.accessPath
                    && c.filters.map { it.name }.contains("roles")
                    &&c.filters.map { it.name }.contains("token");
        }
    }


}