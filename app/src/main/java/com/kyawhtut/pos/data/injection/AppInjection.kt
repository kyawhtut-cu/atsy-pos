package com.kyawhtut.pos.data.injection

import com.kyawhtut.pos.data.db.*
import com.kyawhtut.pos.data.db.entity.user
import com.kyawhtut.pos.ui.authentication.login.LoginRepository
import com.kyawhtut.pos.ui.authentication.login.LoginRepositoryImpl
import com.kyawhtut.pos.ui.authentication.login.LoginViewModel
import com.kyawhtut.pos.ui.category.CategoryRepository
import com.kyawhtut.pos.ui.category.CategoryRepositoryImpl
import com.kyawhtut.pos.ui.category.CategoryViewModel
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialogRepository
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialogRepositoryImpl
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialogViewModel
import com.kyawhtut.pos.ui.home.HomeRepository
import com.kyawhtut.pos.ui.home.HomeRepositoryImpl
import com.kyawhtut.pos.ui.home.HomeViewModel
import com.kyawhtut.pos.ui.superadmin.FunctionRepository
import com.kyawhtut.pos.ui.superadmin.FunctionRepositoryImpl
import com.kyawhtut.pos.ui.superadmin.FunctionViewModel
import com.kyawhtut.pos.ui.user.UserRepository
import com.kyawhtut.pos.ui.user.UserRepositoryImpl
import com.kyawhtut.pos.ui.user.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AppInjection {
    val rootUser = module {
        single(named("rootUser")) {
            user {
                id = -1
                displayName = getProperty("ADMIN_DISPLAY_NAME")
                userName = getProperty("ADMIN_USER_NAME")
                password = getProperty("ADMIN_PASSWORD")
                roleId = 1
            }
        }
    }
    val database = module {
        single {
            provideDB(androidContext())
        }

        single {
            provideUserDao(get())
        }

        single {
            provideRoleDao(get())
        }

        single {
            provideCategoryDao(get())
        }

        single {
            provideProductDao(get())
        }

        single {
            provideCustomerDao(get())
        }

        single {
            provideSellDao(get())
        }
    }

    val preference = module {
        single { (name: String?) ->
            PreferenceInjector().provideSharedPreference(get(), name)
        }
    }

    val authentication = module {
        single<LoginRepository> {
            LoginRepositoryImpl(get(), get(), get(named("rootUser")), get { parametersOf(null) })
        }

        viewModel {
            LoginViewModel(get())
        }
    }

    val home = module {
        single<HomeRepository> {
            HomeRepositoryImpl(get { parametersOf(null) })
        }

        viewModel {
            HomeViewModel(get())
        }
    }

    val category = module {
        single<CategoryAddDialogRepository> {
            CategoryAddDialogRepositoryImpl(get())
        }

        single<CategoryRepository> {
            CategoryRepositoryImpl(get(), get())
        }

        viewModel {
            CategoryAddDialogViewModel(get())
        }

        viewModel {
            CategoryViewModel(get())
        }
    }

    val function = module {
        single<FunctionRepository> {
            FunctionRepositoryImpl(get { parametersOf(null) })
        }

        viewModel { param ->
            FunctionViewModel(param[0], get())
        }
    }

    val user = module {
        single<UserRepository> {
            UserRepositoryImpl(get { parametersOf(null) })
        }

        viewModel {
            UserViewModel(get())
        }
    }
}
