package su.sendandsolve.utils

import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it]) {
        is JSONArray -> {
            val map = (0 until value.length()).associate { Pair( it.toString(), value[it]) }
            JSONObject(map).toMap().values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else -> value
    }
}

fun Map<*, *>.toJSONObject(): JSONObject {
    val jsonObject = JSONObject()
    for ((key, value) in this) {
        when (value) {
            is Map<*, *> -> jsonObject.put(key.toString(), value.toJSONObject())
            is List<*> -> jsonObject.put(key.toString(), value.toJSONArray())
            is JSONObject -> jsonObject.put(key.toString(), value) //already a JSONObject
            is JSONArray -> jsonObject.put(key.toString(), value) //already a JSONArray
            null -> jsonObject.put(key.toString(), JSONObject.NULL)
            else -> jsonObject.put(key.toString(), value)
        }
    }
    return jsonObject
}

fun List<*>.toJSONArray(): JSONArray {
    val jsonArray = JSONArray()
    for (value in this) {
        when (value) {
            is Map<*, *> -> jsonArray.put(value.toJSONObject())
            is List<*> -> jsonArray.put(value.toJSONArray())
            is JSONObject -> jsonArray.put(value)
            is JSONArray -> jsonArray.put(value)
            null -> jsonArray.put(JSONObject.NULL)
            else -> jsonArray.put(value)
        }
    }
    return jsonArray
}