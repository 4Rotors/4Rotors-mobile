package ru.vsu.zmaev.a4rotor.di

import org.koin.dsl.module
import ru.vsu.zmaev.a4rotor.data.repository.MainRepository
import ru.vsu.zmaev.a4rotor.data.repository.MainRepositoryImpl

val appModule = module {
    single<MainRepository> { MainRepositoryImpl(get()) }
    single {  }
}