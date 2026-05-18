package com.yeipi.teoriacolas.domain

import kotlin.math.pow

data class QueueingInput(
    val lambda: Double,
    val mu: Double,
    val servers: Int = 1,
    // Metadatos del estudio
    val projectName: String = "",
    val analyst: String = "",
    val location: String = "",
    val observations: String = ""
)

data class QueueingResult(
    val modelType: String,
    val lambda: Double,
    val mu: Double,
    val servers: Int,
    val rho: Double,
    val p0: Double,
    val lq: Double,
    val l: Double,
    val wq: Double,
    val w: Double,
    val pw: Double? = null,
    // Metadatos
    val projectName: String = "",
    val analyst: String = "",
    val location: String = "",
    val observations: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

object QueueingCalculator {

    fun calculateMM1(input: QueueingInput): QueueingResult {
        val lambda = input.lambda
        val mu = input.mu
        require(mu > 0) { "μ debe ser mayor que 0" }
        require(lambda >= 0) { "λ no puede ser negativa" }

        val rho = lambda / mu
        require(rho < 1) {
            "Sistema inestable: ρ = ${"%.4f".format(rho)} debe ser < 1"
        }

        return QueueingResult(
            modelType = "M/M/1",
            lambda = lambda, mu = mu, servers = 1,
            rho = rho,
            p0 = 1 - rho,
            l  = rho / (1 - rho),
            lq = (rho * rho) / (1 - rho),
            w  = 1 / (mu - lambda),
            wq = lambda / (mu * (mu - lambda)),
            projectName = input.projectName,
            analyst = input.analyst,
            location = input.location,
            observations = input.observations
        )
    }

    fun calculateMMs(input: QueueingInput): QueueingResult {
        val lambda = input.lambda
        val mu = input.mu
        val s = input.servers
        require(mu > 0) { "μ debe ser mayor que 0" }
        require(lambda >= 0) { "λ no puede ser negativa" }
        require(s >= 1) { "El número de servidores debe ser >= 1" }

        val a = lambda / mu
        val rho = a / s
        require(rho < 1) {
            "Sistema inestable: ρ = ${"%.4f".format(rho)} debe ser < 1"
        }

        // P0
        var sumTerms = 0.0
        for (n in 0 until s) sumTerms += a.pow(n) / factorial(n)
        val lastTerm = a.pow(s) / (factorial(s) * (1 - rho))
        val p0 = 1.0 / (sumTerms + lastTerm)

        val lq = (p0 * a.pow(s) * rho) / (factorial(s) * (1 - rho).pow(2))
        val l  = lq + a
        val wq = lq / lambda
        val w  = wq + 1 / mu
        val pw = (a.pow(s) / (factorial(s) * (1 - rho))) * p0

        return QueueingResult(
            modelType = "M/M/$s",
            lambda = lambda,
            mu = mu,
            servers = s,
            rho = rho,
            p0 = p0,
            lq = lq,
            l = l,
            wq = wq,
            w = w,
            pw = pw,
            projectName = input.projectName,
            analyst = input.analyst,
            location = input.location,
            observations = input.observations
        )
    }

    private fun factorial(n: Int): Double {
        var result = 1.0
        for (i in 2..n) result *= i
        return result
    }
}