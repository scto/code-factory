package com.code.factory

import com.code.factory.bridge.BridgeFactory
import com.code.factory.bridge.BridgeFactoryImpl
import com.code.factory.coderesolver.CodeResolver
import com.code.factory.coderesolver.CodeResolverImpl
import com.code.factory.ksp.BasePathProvider
import com.code.factory.ksp.BasePathProviderImpl
import com.code.factory.ksp.KspProcessor
import com.code.factory.ksp.PhaseResolver
import com.code.factory.ksp.PhaseResolverImpl
import com.code.factory.ksp.SleepKspProcessor
import com.code.factory.ksp.SleepKspProcessorImpl
import com.code.factory.ksp.WorkActorImpl
import com.code.factory.ksp.WorkKspProvider
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.assisted.AssistedFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [ProcessorBindModule::class])
interface ProcessorComponent {
    fun getApiKeyResolver(): ApiKeyResolver

    fun getSleepKSPProcessor(): SleepKspProcessor

    fun getKSPProcessor(): KspProcessor

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun logger(logger: KSPLogger): Builder

        @BindsInstance
        fun codeGenerator(codeGenerator: CodeGenerator): Builder

        fun build(): ProcessorComponent
    }
}

@Module
interface ProcessorBindModule {
    @Binds
    abstract fun provideAllDeclarationFinder(impl: AllDeclarationFinderImpl): AllDeclarationFinder

    @Binds
    abstract fun provideCodeResolver(impl: CodeResolverImpl): CodeResolver

    @Binds
    abstract fun provideTestCodeFilter(impl: TestCodeFilterImpl): TestCodeFilter

    @Binds
    abstract fun provideTestFilesResolver(impl: TestFilesResolverImpl): TestFilesResolver

    @Binds
    abstract fun provideCodeFilter(impl: CodeFilterImpl): CodeFilter

    @Binds
    abstract fun provideTestSourcePathResolver(impl: TestSourcePathResolverImpl): TestSourcePathResolver

    @Singleton
    @Binds
    abstract fun provideBasePathProvider(impl: BasePathProviderImpl): BasePathProvider

    @Binds
    abstract fun provideCompileChecker(impl: CompileCheckerImpl): CompileChecker

    @Binds
    abstract fun provideBridgeFactory(impl: BridgeFactoryImpl): BridgeFactory

    @Binds
    abstract fun provideSymbolProcessor(impl: KspProcessor): WorkKspProvider

    @Binds
    abstract fun provideSleepKspProcessor(impl: SleepKspProcessorImpl): SleepKspProcessor

    @Binds
    abstract fun provideInterfaceFinder(impl: InterfaceFinderImpl): InterfaceFinder

    @Binds
    abstract fun providePhaseResolver(impl: PhaseResolverImpl): PhaseResolver

    @Binds
    abstract fun provideApiKeyResolver(impl: ApiKeyResolverImpl): ApiKeyResolver

    @AssistedFactory
    interface WorkActorFactory {
        fun create(
            resolver: Resolver,
            apiKey: String,
        ): WorkActorImpl
    }
}
