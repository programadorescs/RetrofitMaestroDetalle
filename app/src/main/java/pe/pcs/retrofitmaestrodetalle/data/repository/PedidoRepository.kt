package pe.pcs.retrofitmaestrodetalle.data.repository

import pe.pcs.retrofitmaestrodetalle.data.service.PedidoService
import pe.pcs.retrofitmaestrodetalle.data.service.ProductoService

class PedidoRepository {

    private val service = PedidoService()
    private val producto = ProductoService()

}