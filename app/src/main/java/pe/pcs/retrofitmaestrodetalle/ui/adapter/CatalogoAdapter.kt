package pe.pcs.retrofitmaestrodetalle.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.pcs.retrofitmaestrodetalle.core.UtilsCommon
import pe.pcs.retrofitmaestrodetalle.databinding.ItemsCatalogoBinding
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto

class CatalogoAdapter(
    private val iOnClickListener: IOnClickListener
): ListAdapter<Producto, CatalogoAdapter.BindViewHolder>(DiffCallback) {

    interface IOnClickListener {
        fun clickItem(entidad: Producto)
    }

    private object DiffCallback: DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem == newItem
        }
    }

    inner class BindViewHolder(private val binding: ItemsCatalogoBinding): RecyclerView.ViewHolder(binding.root) {
        fun enlazar(entidad: Producto) {
            binding.tvTitulo.text = entidad.descripcion
            binding.tvPrecio.text = UtilsCommon.formatearDoubleString(entidad.precio)

            binding.ibAgregar.setOnClickListener { iOnClickListener.clickItem(entidad) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        return BindViewHolder(
            ItemsCatalogoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        // Otener cada item de la lista
        val item = getItem(position)

        holder.enlazar(item)
    }
}