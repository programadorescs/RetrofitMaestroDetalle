package pe.pcs.retrofitmaestrodetalle.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.pcs.retrofitmaestrodetalle.core.UtilsCommon
import pe.pcs.retrofitmaestrodetalle.data.model.ReporteDetallePedidoModel
import pe.pcs.retrofitmaestrodetalle.databinding.ItemsDetallePedidoRealizadoBinding

class ReporteDetallePedidoAdapter(
    private val lista: List<ReporteDetallePedidoModel>
): RecyclerView.Adapter<ReporteDetallePedidoAdapter.BindViewHolder>() {

    inner class BindViewHolder(private val binding: ItemsDetallePedidoRealizadoBinding): RecyclerView.ViewHolder(binding.root) {
        fun enlazar(entidad: ReporteDetallePedidoModel) {
            binding.tvDescripcion.text = entidad.descripcion
            binding.tvPrecio.text = UtilsCommon.formatearDoubleString(entidad.precio)
            binding.tvCantidad.text = "x ${entidad.cantidad}"
            binding.tvImporte.text = UtilsCommon.formatearDoubleString(entidad.importe)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        return BindViewHolder(
            ItemsDetallePedidoRealizadoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        val item = lista[position]

        holder.enlazar(item)
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}