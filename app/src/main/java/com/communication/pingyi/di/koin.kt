package com.communication.pingyi.di

import com.communication.lib_http.api.httpModule
import com.communication.pingyi.ui.contact.contact.ContactRepository
import com.communication.pingyi.ui.contact.contact.ContactViewModel
import com.communication.pingyi.ui.home.AppsViewModel
import com.communication.pingyi.ui.home.HomeAppsRepository
import com.communication.pingyi.ui.login.account.LoginRepository
import com.communication.pingyi.ui.login.account.LoginViewModel
import com.communication.pingyi.ui.me.changepwd.ChangePwdRepository
import com.communication.pingyi.ui.me.changepwd.ChangePwdViewModel
import com.communication.pingyi.ui.me.me.MeRepository
import com.communication.pingyi.ui.me.me.MeViewModel
import com.communication.pingyi.ui.message.MessageRepository
import com.communication.pingyi.ui.message.MessageViewModel
import com.communication.pingyi.ui.update_version.UpdateVersionRepository
import com.communication.pingyi.ui.update_version.UpdateVersionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by LG
 * on 2022/3/4  11:39
 * Description：
 */

val viewModelModule = module {

    viewModel{LoginViewModel(get())}
    viewModel{ MeViewModel(get()) }
    viewModel { AppsViewModel(get()) }
    viewModel { MessageViewModel(get()) }
    viewModel { ContactViewModel(get()) }
    viewModel { ChangePwdViewModel(get()) }
    viewModel { UpdateVersionViewModel(get()) }

}

val repositoryModule = module {
    single { LoginRepository(get()) }
    single { MeRepository(get()) }
    single { HomeAppsRepository(get()) }
    single { MessageRepository(get()) }
    single { ContactRepository(get()) }
    single { ChangePwdRepository(get()) }
    single { UpdateVersionRepository(get()) }
}

val allModule = listOf(viewModelModule,repositoryModule,httpModule)