package com.lge.systraceapp

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception


val TAG = "SystraceApp:FileViewer"
class TraceFileViewer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trace_file_viewer)

        val btn:Button = findViewById(R.id.button)
        val editText:EditText = findViewById(R.id.editText)

        btn.setOnClickListener {

            var text: String? = null
            var length: Int? = 0

            // null check by "?"
            text =  editText?.getText()?.toString()
            length = text?.length


            val toast = Toast.makeText(this, text + length, Toast.LENGTH_SHORT)
            toast.show()

        }

        // list in kotlin, Immutable 대입 불가
        var list : List<Int> = List(5, {i -> i})
        for (i in 0 until list.size) {
            Log.d(TAG, list.get(i).toString())
        }

        for (i in list.indices)
            Log.d(TAG, list.get(i).toString())


        // list in kotlin, Mutable
        var list_mutable : MutableList<String> = mutableListOf()
        list_mutable.add("test")
        list_mutable.add("test2")

        list_mutable.set(0, "test0")

        for (i in list_mutable.indices)
            Log.d(TAG, list_mutable.get(i) + " size: " + list_mutable.size)

        var list_mutable_contruct : MutableList<ConstructTest> = mutableListOf(
                ConstructTest("11", "22", 12),
                ConstructTest("13", "23", 13)
        )

        list_mutable_contruct.add(2, ConstructTest("14", "24", 14))


        for (i in list_mutable_contruct.indices)
            Log.d(TAG, list_mutable_contruct.get(i).firstName)




        PersonDatabase.saveData(Person.makePerson("trrr", 16))
        PersonDatabase.saveData(Person.makePerson("trr22r", 17))

        try {
            var list_person: MutableList<Person> = PersonDatabase.loadAllData()

            for (i in list_person.indices)
                Log.d(TAG, list_person.get(i).name)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /* test code for coroutine */
        coroutine()

        someTask(this).execute()


    }


}

class ConstructTest (val firstName: String, val lastName: String, var age: Int)

class Person private constructor(val name: String, val age: Int)
{
    // 이름이 없는 object입니다.
    // 이 안에 선언된 프로퍼티와 메서드는
    // Person의 static 프로퍼티/메서드인 것처럼 접근할 수 있습니다.
    companion object
    {
        @JvmStatic
        fun makePerson(name: String, age: Int): Person
        {
            return Person(name, age)
        }
    }
}

object PersonDatabase
{
    var person: MutableList<Person> = mutableListOf()

    @JvmStatic
    fun saveData(data: Person)
    {
        person.add(data)
    }
    @JvmStatic
    fun loadAllData(): MutableList<Person>
    {
        // 데이터베이스에서 데이터 읽는 로직
        return person
    }
}

fun coroutine() {
    Log.d(TAG,"Start")

// Start a coroutine
    GlobalScope.launch {
        runBlocking {
            delay(10)
        }
        Log.d(TAG, "Hello")
    }
    Thread.sleep(10) // wait for 2 seconds
    Log.d(TAG,"Stop")
}

class someTask(context: Context) : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        // ...
        var i : Int = 0
        while(true) {
            i++

            if (i == 100) {
                Log.d(TAG, "break")
                break
            }
        }

        return null
    }

    override fun onPreExecute() {
        Log.d(TAG, "onPreExecute")
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        Log.d(TAG, "onPostExecute")
        super.onPostExecute(result)
        // ...
    }
}
