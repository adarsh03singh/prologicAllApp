package com.csestateconnect.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.csestateconnect.R
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.events.CreateEventData
import com.csestateconnect.db.data.events.GetEventCategoriesList
import com.csestateconnect.db.data.events.eventsDataList.GetEventsDataEntity
import com.csestateconnect.repo.AuthRepository
import com.csestateconnect.repo.EventsRepository
import com.csestateconnect.utils.AlarmBroadcast
import com.csestateconnect.utils.Coroutines
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class EventsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventsRepository
    private val authRepository: AuthRepository
    var timeTonotify =  MutableLiveData<String>()
    var progressBar = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var cancelablebutton = MutableLiveData<Boolean>().apply { postValue(true) }


    //for Edit client profile from server
    var previousIdForUpdateEvent = MutableLiveData<Int>()
    var eventCategoriesId = MutableLiveData<String>()
    var eventCategoriesName = MutableLiveData<String>()
    var eventAutoReminder = MutableLiveData<String>()
    var eventActiveStatus = MutableLiveData<String>()

    var eventAutoRemindertrue =  MutableLiveData<Boolean>().apply { postValue(false) }
    var eventAutoReminderfalse =  MutableLiveData<Boolean>().apply { postValue(false) }
    var eventActiveStatustrue =  MutableLiveData<Boolean>().apply { postValue(false) }
    var eventActiveStatusFalse =  MutableLiveData<Boolean>().apply { postValue(false) }

    var eventAutoReminderChecked = MutableLiveData<Boolean>().apply { postValue(false) }
    var eventActiveStatusChecked = MutableLiveData<Boolean>().apply { postValue(false) }

    var eventDate = MutableLiveData<String>()
    var eventTime = MutableLiveData<String>()
    var eventSubject = MutableLiveData<String>().apply { postValue("") }
    var eventDescription = MutableLiveData<String>()
    var eventPriority = MutableLiveData<String>()

    //CREATE/UPDATE CLIENT DOC
    var document_name = MutableLiveData<String>()


    val sharedPref: SharedPreferences = application.getSharedPreferences("tokenValue", 0)
    val token = "Token " + sharedPref.getString("tokenValue", "")

    init {
        var clientOldData: List<com.csestateconnect.db.data.events.eventsDataList.Result?>? = null
        authRepository = AuthRepository(application)
        repository = EventsRepository(application)
        Coroutines.io {

            try {
                repository.getEventDataFromLiveData()
                clientOldData = repository.getEventDataFromList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
             Coroutines.main {
                try {
                clientOldData!!.forEach {
                    if (it!!.id!!.equals(previousIdForUpdateEvent.value)) {
//                        if (it!!.id != null) {
//                            count = 1
//                        }
                        eventDate.value = it!!.date
                        eventTime.value = it!!.time
                        eventSubject.value = it!!.subject
                        eventDescription.value = it!!.description
                        eventPriority.value = it!!.priority.toString()

                        eventActiveStatus.value = it!!.active.toString()
                        eventActiveStatustrue.value = it!!.active
                        if(it!!.active!!.equals(false)) {
                            eventActiveStatusFalse.value = true
                        }

                        eventAutoReminder.value = it!!.autoReminder.toString()
                        eventAutoRemindertrue.value = it!!.autoReminder
                        if(it!!.autoReminder!!.equals(false)) {
                            eventAutoReminderfalse.value = true
                        }
                        eventAutoReminder.value = it!!.autoReminder.toString()
                        eventCategoriesName.value = it!!.category!!.name
                        eventCategoriesId.value = it!!.category!!.id.toString()
                    }
                }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

        fun callEventCategoriesListAPIAndStore() {
            Coroutines.main {
                repository.getEventCategoriesListApi()
            }
        }

        fun getEventCategoriesListFromDatabase(): LiveData<GetEventCategoriesList> {
            return repository.getEventCategoriesListData()
        }

        fun callEventdataListAPIAndStore() {
            Coroutines.main {
                repository.getEventDataListApi()
            }
        }

    fun getEventDataListFromDatabase(): LiveData<GetEventsDataEntity> {
        return repository.getEventDataFromLiveData()
    }


         fun checkfieldsForAddEvents(view: View){
        if (eventCategoriesId.value.isNullOrEmpty()
            || eventCategoriesId.value.isNullOrEmpty()  || eventAutoReminder.value.isNullOrEmpty()
            || eventActiveStatus.value.isNullOrEmpty() || eventDate.value.isNullOrEmpty()
            || eventTime.value.isNullOrEmpty() || eventSubject.value.isNullOrEmpty()|| eventDescription.value.isNullOrEmpty()
            || eventPriority.value.isNullOrEmpty()
        ) {

            Snackbar.make(view, "Please fill all the details", Snackbar.LENGTH_LONG).show()
        }
            else {
            cancelablebutton.value = false
            createEvents(view)
        }
    }


        fun createEvents(view: View) {
        progressBar.value = View.VISIBLE
            var response: Response<CreateEventData>?
            Coroutines.io {
                response = repository.getCreateEvent(
                    eventCategoriesId.value!!,
                    eventAutoReminder.value!!.toBoolean(),
                    eventActiveStatus.value!!.toBoolean(),
                    eventDate.value!!,
                    eventTime.value,
                    eventSubject.value,
                    eventDescription.value,
                    eventPriority.value!!
                )
                if ( !eventAutoReminder.value!!.equals("False")){
                setAlarm(eventSubject.value, eventDescription.value, eventPriority.value!!,eventCategoriesId.value!!,
                    eventDate.value!!, eventTime.value,view)
                }

                if (response!!.isSuccessful) {
                    Coroutines.main {

                    callEventdataListAPIAndStore()
                        Snackbar.make(view, "Event Created Successfully", Snackbar.LENGTH_LONG)
                            .show()
                        goToEventListFragment(view)
                    progressBar.value = View.GONE

                    }
                } else {
                    Coroutines.main {

                        try {
                            if (!response!!.errorBody()!!.equals(null)) {
                            cancelablebutton.value = true
                                val jObjError = JSONObject(response!!.errorBody()!!.string())
                                val error = jObjError.getString("error")
                                val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                                snackbar.show()

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    progressBar.value = View.GONE
                    }
                }
            }

        }

    fun checkfieldsForUpdateEvents(view: View){
        if (eventCategoriesId.value.isNullOrEmpty()
            || eventCategoriesId.value.isNullOrEmpty()  || eventAutoReminder.value.isNullOrEmpty()
            || eventActiveStatus.value.isNullOrEmpty() || eventDate.value.isNullOrEmpty()
            || eventTime.value.isNullOrEmpty() || eventSubject.value.isNullOrEmpty()|| eventDescription.value.isNullOrEmpty()
            || eventPriority.value.isNullOrEmpty()
        ) {

            Snackbar.make(view, "Please fill all the details", Snackbar.LENGTH_LONG).show()
        }
        else {
            cancelablebutton.value = false
            updateEventt(view)
        }
    }

    fun updateEventt(view: View){

        progressBar.value = View.VISIBLE
        var response : Response<CreateEventData>?
        Coroutines.io {
            response = repository.updateEvents( previousIdForUpdateEvent.value!!,eventCategoriesId.value!!,
                eventAutoReminder.value!!.toBoolean(),
                eventActiveStatus.value!!.toBoolean(),
                eventDate.value!!,
                eventTime.value,
                eventSubject.value,
                eventDescription.value,
                eventPriority.value!!)

            if ( !eventAutoReminder.value!!.equals("False")){
                setAlarm(eventSubject.value, eventDescription.value, eventPriority.value!!,eventCategoriesId.value!!,
                    eventDate.value!!, eventTime.value,view)
            }

            if (response!!.isSuccessful){
                Coroutines.main {

                    callEventdataListAPIAndStore()
                    Snackbar.make(view, "Event Updated", Snackbar.LENGTH_LONG).show()

                    goToEventListFragment(view)
                    progressBar.value = View.GONE

                }
            } else {
                Coroutines.main {

                    try {
                        if (!response!!.errorBody()!!.equals(null)) {
                            cancelablebutton.value = true
                            val jObjError = JSONObject(response!!.errorBody()!!.string())
                            val error = jObjError.getString("error")
                            val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                            snackbar.show()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    progressBar.value = View.GONE
                }
            }
        }

    }
        fun getCountriesData(): LiveData<List<Countries>> {
            return authRepository.getallLiveDataCountriesDatabase()
        }

        fun callRemoveEventApi(eventId: Int,view: View) {
                Coroutines.main {
                    val response = repository.removeEvents(eventId)
                    if (response!!.isSuccessful) {
                        Snackbar.make(
                            view!!,
                            "Event has been removed.",
                            Snackbar.LENGTH_LONG
                        ).show()

                    }
                }

        }

        fun goToEventListFragment(view: View) {
            view.findNavController().navigate(R.id.horizontalCalenderFragment)
        }

    fun setAlarm(subject: String?,descript: String?,priority: String?,category: String?, date: String?, time: String?, view: View): kotlin.Unit {
        val am: AlarmManager? =
            view.context!!.getSystemService(android.content.Context.ALARM_SERVICE) as AlarmManager?
        val intent: Intent? = Intent(view.context!!, AlarmBroadcast::class.java)
        intent!!.putExtra("subject", subject)
        intent!!.putExtra("descript", descript)
        intent!!.putExtra("priority", priority)
        intent!!.putExtra("category", category)
        intent.putExtra("time", time)
        intent.putExtra("date", date)
        val pendingIntent: PendingIntent? = PendingIntent.getBroadcast(
            view.context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val dateandtime: String? = date + " " + timeTonotify.value
        val formatter: java.text.DateFormat? = SimpleDateFormat("d-M-yyyy hh:mm")
        try {
            val date1: Date? = formatter!!.parse(dateandtime)
            if (Build.VERSION.SDK_INT >= 23) {
                am!!.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date1!!.time, pendingIntent)
            } else if (Build.VERSION.SDK_INT >= 19) {
                am!!.setExact(AlarmManager.RTC_WAKEUP, date1!!.time, pendingIntent)
            } else {
                am!!.set(AlarmManager.RTC_WAKEUP, date1!!.time, pendingIntent)
            }
//            val date1: Date? = formatter!!.parse(dateandtime)
//            am!!.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, , pendingIntent)
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }
    }

}