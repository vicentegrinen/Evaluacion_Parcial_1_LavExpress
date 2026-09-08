package com.example.lavexpress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lavexpress.ui.theme.LavExpressTheme

//Aqui asigno una clase padre para despues pasar la funcion cobro y aplico las condisiones a cada uno entonces creo un cobro base para despues poder editarlo
open class Maquinaria(val slot: String, val nombre: String, val estado: Int, val turno: Int, val tarifaBase: Int, val tipoUsuario: String, val tiempo: Int) {
    open fun cobro(): Double {
        val conIva = ((tiempo / 60.0) * tarifaBase) * 1.19
        return if (tipoUsuario.equals("empresa", true)) conIva * 0.5 else conIva
    }
}

// ocupe in if en la variable efectivo para ir cambiando el precio por el tiempo que pide que menos de 30 minus se paga 0
class Secadora(slot: String, nombre: String, estado: Int, turno: Int, tarifaBase: Int, tipoUsuario: String, tiempo: Int) : Maquinaria(slot, nombre, estado, turno, tarifaBase, tipoUsuario, tiempo) {
    override fun cobro(): Double {
        val efectivo = if (tiempo < 30) 0.0 else (tiempo / 60.0) * tarifaBase
        val conIva = efectivo * 1.19
        return if (tipoUsuario.equals("empresa", true)) conIva * 0.5 else conIva
    }
}
//aqui aplico el descuento de precio con el tiempo de uso y despues le aplico el iva
class Lavadora(slot: String, nombre: String, estado: Int, turno: Int, tarifaBase: Int, tipoUsuario: String, tiempo: Int) : Maquinaria(slot, nombre, estado, turno, tarifaBase, tipoUsuario, tiempo) {
    override fun cobro(): Double {
        val tEfectivo = if (tipoUsuario.equals("suscriptor", true)) tiempo * 0.8 else tiempo.toDouble()
        val conIva = ((tEfectivo / 60.0) * tarifaBase) * 1.19
        return if (tipoUsuario.equals("empresa", true)) conIva * 0.5 else conIva
    }
}


// en esta clase declaro con un boolean que si el lavado es con vapor se aplique el 30%
class LavasecaIndustrial(slot: String, nombre: String, estado: Int, turno: Int, tarifaBase: Int, tipoUsuario: String, tiempo: Int, val vapor: Boolean) : Maquinaria(slot, nombre, estado, turno, tarifaBase, tipoUsuario, tiempo) {
    override fun cobro(): Double {
        var base = (tiempo / 60.0) * tarifaBase
        if (vapor) base *= 1.3
        val conIva = base * 1.19
        return if (tipoUsuario.equals("empresa", true)) conIva * 0.5 else conIva
    }
}
class MainActivity : ComponentActivity() {

    // Aqui intancio todas las maquinas que existen
    val maquina1: Maquinaria = Lavadora(slot = "LV12CD", nombre = "Samsung WW90", estado = 1, turno = 1, tarifaBase = 1200, tipoUsuario = "Suscriptor", tiempo = 75)
    val maquina2: Maquinaria = Lavadora(slot = "LV99ZA", nombre = "LG F4WV509", estado = 1, turno = 1, tarifaBase = 1200, tipoUsuario = "regular", tiempo = 180)
    val maquina3: Maquinaria = Secadora(slot = "SC22TO", nombre = "Bosch WTH85200", estado = 1, turno = 1, tarifaBase = 1200, tipoUsuario = "regular", tiempo = 25)
    val maquina4: Maquinaria = LavasecaIndustrial(slot = "LI44RG", nombre = "Miele PW6", estado = 1, turno = 1, tarifaBase = 1200, tipoUsuario = "empresa", tiempo = 120, vapor = true)
    val maquina5: Maquinaria = LavasecaIndustrial(slot = "LI77RG", nombre = "Speed Queen SF7", estado = 1, turno = 1, tarifaBase = 1200, tipoUsuario = "regular", tiempo = 45, vapor = false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LavExpressTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        val resultado1 = mostrarMaquinaria(maquina1)
        val resultado2 = mostrarMaquinaria(maquina2)
        val resultado3 = mostrarMaquinaria(maquina3)
        val resultado4 = mostrarMaquinaria(maquina4)
        val resultado5 = mostrarMaquinaria(maquina5)
    }

    private fun mostrarMaquinaria(m: Maquinaria): String {
        return "Slot: ${m.slot}, Nombre: ${m.nombre}, Cobro Final: ${m.cobro()}"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LavExpressTheme {
        Greeting("Android")
    }
}