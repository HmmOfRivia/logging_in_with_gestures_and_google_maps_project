package pl.lipov.laborki

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.lipov.laborki.common.utils.GestureDetectorUtils
import pl.lipov.laborki.common.utils.MapUtils
import pl.lipov.laborki.common.utils.SensorEventsUtils
import pl.lipov.laborki.data.LoginApi
import pl.lipov.laborki.data.LoginRepository
import pl.lipov.laborki.data.MapRepository
import pl.lipov.laborki.data.Building
import pl.lipov.laborki.presentation.Map.BuildingAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import pl.lipov.laborki.presentation.Map.MapsViewModel
import pl.lipov.laborki.presentation.MainViewModel

private const val LOGIN_API_ENDPOINT = "http:/test.pl"

val utilsModule = module {
    single { MapUtils(context = get(), mapRepository = get()) }
    single { GestureDetectorUtils() }
    factory { provideSensorManager(context = get()) }
    factory { provideAccelerometer(sensorManager = get()) }
    single { SensorEventsUtils(sensorManager = get(), accelerometer = get()) }
    single {Building(iconResId = get(),name = get(), coord = get())}
}

private fun provideSensorManager(
    context: Context
): SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

private fun provideAccelerometer(
    sensorManager: SensorManager
): Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

val networkModule = module {
    factory { provideOkHttpClient() }
    single { provideGson() }
    single { provideRetrofit(okHttpClient = get(), gson = get()) }
    factory { provideLoginApi(get()) }
}

private fun provideOkHttpClient(): OkHttpClient = OkHttpClient().newBuilder().build()

private fun provideGson(): Gson = GsonBuilder().serializeNulls().setLenient().create()

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gson: Gson
): Retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(LOGIN_API_ENDPOINT)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

private fun provideLoginApi(
    retrofit: Retrofit
): LoginApi = retrofit.create(LoginApi::class.java)

val repositoriesModule = module {
    single { LoginRepository(loginApi = get()) }
    single { MapRepository() }
}


val viewModelsModule = module {
    viewModel {
        /*MapsViewModel(
            mapUtils = get(),
            mapRepository = get()
        )*/
        MainViewModel(
            gestureDetectorUtils = get(),
            sensorEventsUtils = get(),
            loginRepository = get()
        )
    }
}
