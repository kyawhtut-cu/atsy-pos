package com.kyawhtut.pos.data.injection

import com.kyawhtut.pos.BuildConfig
import com.kyawhtut.pos.data.api.network.ApiRetrofit
import com.kyawhtut.pos.data.api.network.InternetConnectivityInterceptor
import com.kyawhtut.pos.data.db.*
import com.kyawhtut.pos.data.db.entity.user
import com.kyawhtut.pos.ui.category.CategoryRepository
import com.kyawhtut.pos.ui.category.CategoryRepositoryImpl
import com.kyawhtut.pos.ui.category.CategoryViewModel
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialogRepository
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialogRepositoryImpl
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialogViewModel
import com.kyawhtut.pos.ui.customer.CustomerRepository
import com.kyawhtut.pos.ui.customer.CustomerRepositoryImpl
import com.kyawhtut.pos.ui.customer.CustomerViewModel
import com.kyawhtut.pos.ui.function.FunctionRepository
import com.kyawhtut.pos.ui.function.FunctionRepositoryImpl
import com.kyawhtut.pos.ui.function.FunctionViewModel
import com.kyawhtut.pos.ui.home.*
import com.kyawhtut.pos.ui.login.LoginRepository
import com.kyawhtut.pos.ui.login.LoginRepositoryImpl
import com.kyawhtut.pos.ui.login.LoginViewModel
import com.kyawhtut.pos.ui.product.ProductRepository
import com.kyawhtut.pos.ui.product.ProductRepositoryImpl
import com.kyawhtut.pos.ui.product.ProductViewModel
import com.kyawhtut.pos.ui.sale.SaleRepository
import com.kyawhtut.pos.ui.sale.SaleRepositoryImpl
import com.kyawhtut.pos.ui.sale.SaleViewModel
import com.kyawhtut.pos.ui.table.TableRepository
import com.kyawhtut.pos.ui.table.TableRepositoryImpl
import com.kyawhtut.pos.ui.table.TableViewModel
import com.kyawhtut.pos.ui.ticket.TicketRepository
import com.kyawhtut.pos.ui.ticket.TicketRepositoryImp
import com.kyawhtut.pos.ui.ticket.TicketViewModel
import com.kyawhtut.pos.ui.welcome.WelcomeRepository
import com.kyawhtut.pos.ui.welcome.WelcomeRepositoryImpl
import com.kyawhtut.pos.ui.welcome.WelcomeViewModel
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

    val network = module {

        single {
            InternetConnectivityInterceptor(get())
        }

        single {
            ApiRetrofit().provideApi(get(), BuildConfig.API_BASE_URL, get())
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

        single {
            provideTicketDao(get())
        }

        single {
            provideCartDao(get())
        }
    }

    val preference = module {
        single { (name: String?) ->
            PreferenceInjector().provideSharedPreference(get(), name)
        }
    }

    val main = module {
        single<HomeRepository> {
            HomeRepositoryImpl(get { parametersOf(null) }, get(named("rootUser")), get(), get())
        }

        single<MainRepository> {
            MainRepositoryImpl(get { parametersOf(null) }, get(named("rootUser")), get())
        }

        viewModel {
            HomeViewModel(get())
        }

        viewModel {
            MainViewModel(get())
        }
    }

    val welcome = module {

        single<WelcomeRepository> {
            WelcomeRepositoryImpl(get { parametersOf(null) }, get())
        }

        viewModel {
            WelcomeViewModel(get())
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
        single<SaleRepository> {
            SaleRepositoryImpl(get { parametersOf(null) }, get(named("rootUser")))
        }

        viewModel {
            SaleViewModel(get())
        }
    }

    val category = module {
        single<CategoryAddDialogRepository> {
            CategoryAddDialogRepositoryImpl(
                get(),
                get { parametersOf(null) },
                get(named("rootUser"))
            )
        }

        single<CategoryRepository> {
            CategoryRepositoryImpl(get { parametersOf(null) }, get(named("rootUser")), get(), get())
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
            FunctionRepositoryImpl(get { parametersOf(null) }, get(named("rootUser")), get())
        }

        viewModel { param ->
            FunctionViewModel(param[0], get())
        }
    }

    val table = module {
        single<TableRepository> {
            TableRepositoryImpl(get { parametersOf(null) }, get(named("rootUser")), get())
        }

        viewModel {
            TableViewModel(get())
        }
    }

    val product = module {
        single<ProductRepository> {
            ProductRepositoryImpl(get { parametersOf(null) }, get(named("rootUser")), get(), get())
        }

        viewModel {
            ProductViewModel(get())
        }
    }

    val customer = module {
        single<CustomerRepository> {
            CustomerRepositoryImpl(get { parametersOf(null) }, get(named("rootUser")), get())
        }

        viewModel {
            CustomerViewModel(get())
        }
    }

    val ticket = module {
        single<TicketRepository> {
            TicketRepositoryImp(
                get { parametersOf(null) },
                get(named("rootUser")),
                get(),
                get(),
                get(),
                get(),
                get()
            )
        }

        viewModel {
            TicketViewModel(get())
        }
    }
}
