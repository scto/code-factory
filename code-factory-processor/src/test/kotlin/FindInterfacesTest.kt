import com.code.factory.interfaceFinder
import kotlin.test.Test
import kotlin.test.assertEquals

class FindInterfacesTest {
    @Test
    fun `simple file should compile`() {
        val source = """
            package test

            class SampleClass {
                fun doSomething() {}
            }
            """

        source compile {}
    }

    private val testSource = """
        package somePackage
        
        interface OneInterface {
            fun one()
        }
        
        class OneInterfaceImpl : OneInterface {
            
            override fun one() {
            
            }
         }
        
        interface InterfaceWithOutImplementation {
            fun one()
        }
        
    """

    @Test
    fun `should find two interfaces`() {
        testSource compile { resolver ->
            val interfaceFinder = interfaceFinder()
            val interfaceNames =
                interfaceFinder.getInterfacesWithOutImplementation(resolver).map {
                    it.qualifiedName!!.getShortName()
                }.toList()
            assertEquals(listOf("InterfaceWithOutImplementation"), interfaceNames)
            assertEquals(
                "somePackage",
                interfaceFinder.getInterfacesWithOutImplementation(resolver).first().packageName.getShortName(),
            )
        }
    }
}
