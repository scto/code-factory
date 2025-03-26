import com.code.factory.ApiKeyResolver
import com.code.factory.ProcessorBindModule
import com.code.factory.ksp.KspProcessor
import com.code.factory.ksp.SleepKspProcessor
import com.code.factory.ksp.WorkActorImpl
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.*

class KspProcessorTest : StringSpec({

    val workActorFactory = mockk<ProcessorBindModule.WorkActorFactory>()
    val sleepKspProcessor = mockk<SleepKspProcessor>()
    val apiKeyResolver = mockk<ApiKeyResolver>()
    val resolver = mockk<Resolver>(relaxed = true)

    val workActor = mockk<WorkActorImpl>()

    val kspProcessor =
        KspProcessor(
            workActorFactory = workActorFactory,
            sleepKspProcessor = sleepKspProcessor,
            apiKeyResolver = apiKeyResolver,
        )

    "should process using workActor when apiKey is resolved" {
        val apiKey = "my-api-key"
        val resultList = listOf(mockk<KSAnnotated>())

        every { apiKeyResolver.resolve(resolver) } returns apiKey
        every { workActorFactory.create(resolver, apiKey) } returns workActor
        every { workActor.act() } returns resultList

        val result = kspProcessor.process(resolver)

        result shouldContainExactly resultList

        verify { workActorFactory.create(resolver, apiKey) }
        verify { workActor.act() }
        verify { apiKeyResolver.resolve(resolver) }
        confirmVerified(workActorFactory, workActor, apiKeyResolver, sleepKspProcessor)
    }

    "should delegate to sleepKspProcessor when apiKey is not resolved" {
        val fallbackList = listOf(mockk<KSAnnotated>())

        every { apiKeyResolver.resolve(resolver) } returns null
        every { sleepKspProcessor.process(resolver) } returns fallbackList

        val result = kspProcessor.process(resolver)

        result shouldContainExactly fallbackList

        verify { sleepKspProcessor.process(resolver) }
        verify { apiKeyResolver.resolve(resolver) }
        confirmVerified(workActorFactory, workActor, apiKeyResolver, sleepKspProcessor)
    }
})
