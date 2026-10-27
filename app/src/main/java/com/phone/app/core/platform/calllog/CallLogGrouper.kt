package com.phone.app.core.platform.calllog

data class GroupedCall(
    val key: String,
    val displayName: String?,
    val number: String,
    val count: Int,
    val latest: CallRecord,
    val types: Set<Int>
)

object CallLogGrouper {
    fun group(records: List<CallRecord>): List<GroupedCall> {
        val map = linkedMapOf<String, MutableList<CallRecord>>()
        for (r in records) {
            val key = r.number.filter { it.isDigit() }.ifBlank { r.number }
            map.getOrPut(key) { mutableListOf() }.add(r)
        }
        return map.map { (key, recs) ->
            GroupedCall(
                key = key,
                displayName = recs.firstOrNull()?.cachedName,
                number = recs.firstOrNull()?.number ?: key,
                count = recs.size,
                latest = recs.maxByOrNull { it.date } ?: recs.first(),
                types = recs.map { it.type }.toSet()
            )
        }.sortedByDescending { it.latest.date }
    }
}
