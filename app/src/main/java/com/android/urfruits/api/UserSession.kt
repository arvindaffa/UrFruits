//import android.content.Context
//import android.content.SharedPreferences
//
//object UserSession {
//    private const val PREF_NAME = "user_session"
//    private const val KEY_TOKEN = "token"
//    private const val KEY_USER_ID = "user_id"
//    private const val KEY_USER_EMAIL = "user_email"
//    private const val KEY_USER_NAME = "user_name"
//
//    private lateinit var preferences: SharedPreferences
//
//    fun init(context: Context) {
//        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//    }
//
//    var token: String?
//        get() = preferences.getString(KEY_TOKEN, null)
//        set(value) {
//            preferences.edit().putString(KEY_TOKEN, value).apply()
//        }
//
//    var userId: String?
//        get() = preferences.getString(KEY_USER_ID, null)
//        set(value) {
//            preferences.edit().putString(KEY_USER_ID, value).apply()
//        }
//
//    var userEmail: String?
//        get() = preferences.getString(KEY_USER_EMAIL, null)
//        set(value) {
//            preferences.edit().putString(KEY_USER_EMAIL, value).apply()
//        }
//
//    var userName: String?
//        get() = preferences.getString(KEY_USER_NAME, null)
//        set(value) {
//            preferences.edit().putString(KEY_USER_NAME, value).apply()
//        }
//
//    fun clear() {
//        preferences.edit().clear().apply()
//    }
//}
