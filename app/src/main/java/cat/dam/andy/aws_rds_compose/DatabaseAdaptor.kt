package cat.dam.andy.aws_rds_compose

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DatabaseAdaptor {
    private val TAG = "DatabaseAdaptor"
    private val SERVER = AWS_KEYS.server
    private val PORT = AWS_KEYS.port
    private val DATABASE = AWS_KEYS.database
    private val USER = AWS_KEYS.user
    private val PASS = AWS_KEYS.pass
    private val URL = "jdbc:mysql://$SERVER:$PORT/$DATABASE"
    private var res: String? = null

    fun getResults(mainActivity: MainActivity, vararg params: String) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            res = getValues(*params) // desempaqueta els elements de l'array
            handler.post {
                Log.d(TAG, "onPostExecute")
                Log.d(TAG, res!!)
                mainActivity.displayResults(res!!)
            }
        }
    }

    private fun getValues(vararg params: String): String {
        try {
            Class.forName("com.mysql.jdbc.Driver")
            val connection: Connection = DriverManager.getConnection(URL, USER, PASS)
            println("Database Connection success " + params.contentToString())
            val result = StringBuilder("RESULTS")
            result.append(System.lineSeparator()).append("=".repeat(result.length - 1))
            result.append(System.lineSeparator())
            val searchPartial = params[0] + "%"
            val st: Statement = connection.createStatement()
            val rs: ResultSet = st.executeQuery("SELECT * FROM POKEMON" +
                    " WHERE " +
                    "name LIKE '$searchPartial'")
            var nResults = 0
            while (rs.next()) {
                try {
                    result.append(rs.getString("name")).append(" (").append(
                        rs.getInt("pokedex_number")).append(")")
                    nResults++
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                result.append(System.lineSeparator())
            }
            result.append(System.lineSeparator())
            result.append("We found ").append(nResults)
                .append(" results that match your query '").append(params[0]).append("'")
            res = result.toString()
            if (res!!.length > 502) {
                Log.d(TAG, "Database Result success " + result.substring(0, 500))
            } else {
                Log.d(TAG, "Database Result success $result")
            }
            connection.close()
        } catch (e: Exception) {
            e.printStackTrace()
            res = e.toString()
        }
        return res!!
    }
}