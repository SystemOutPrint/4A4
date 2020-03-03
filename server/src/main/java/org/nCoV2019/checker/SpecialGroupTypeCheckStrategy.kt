package org.nCoV2019.checker

import org.nCoV2019.domain.GroupType

interface SpecialGroupTypeCheckStrategy : GroupTypeCheckStrategy {

    fun support(cur: GroupType, prev: GroupType): Boolean
}