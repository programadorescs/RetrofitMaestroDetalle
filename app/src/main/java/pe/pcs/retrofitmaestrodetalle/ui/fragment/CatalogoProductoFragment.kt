package pe.pcs.retrofitmaestrodetalle.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pe.pcs.retrofitmaestrodetalle.R
import pe.pcs.retrofitmaestrodetalle.core.UtilsCommon
import pe.pcs.retrofitmaestrodetalle.core.UtilsMessage
import pe.pcs.retrofitmaestrodetalle.data.model.DetallePedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.databinding.FragmentCatalogoProductoBinding
import pe.pcs.retrofitmaestrodetalle.ui.adapter.CatalogoAdapter
import pe.pcs.retrofitmaestrodetalle.ui.dialog.CantidadDialog
import pe.pcs.retrofitmaestrodetalle.ui.viewmodel.PedidoViewModel


class CatalogoProductoFragment : Fragment(), CatalogoAdapter.IOnClickListener,
    CantidadDialog.IEnviarListener {

    private lateinit var binding: FragmentCatalogoProductoBinding
    private val viewModel: PedidoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogoProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        binding.rvLista.adapter = CatalogoAdapter(this)

        viewModel.listaProducto.observe(viewLifecycleOwner) {
            (binding.rvLista.adapter as CatalogoAdapter).submitList(
                Gson().fromJson<List<ProductoModel>>(
                    it.body()!!.data, object: TypeToken<List<ProductoModel>>(){}.type
                )
            )
        }

        viewModel.progressBar.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        viewModel.mErrorStatus.observe(viewLifecycleOwner) {
            if(!it.isNullOrEmpty()) {
                UtilsMessage.showAlertOk(
                    "ERROR", it, requireContext()
                )

                viewModel.mErrorStatus.postValue("")
            }
        }

        viewModel.operacionExitosa.observe(viewLifecycleOwner) {
            if(it != null) {
                if (it.isSuccess) {
                    UtilsMessage.showToast(it.message)
                } else
                    UtilsMessage.showAlertOk("ADVERTENCIA", it.message, requireContext())

                viewModel.operacionExitosa.postValue(null)
            }
        }

        viewModel.totalItem.observe(viewLifecycleOwner) {
            binding.fabCarrito.text = "Carrito [ ${it} ]"
        }

        binding.tilBuscar.setEndIconOnClickListener {
            binding.etBuscar.setText("")
            UtilsCommon.ocultarTeclado(it)
        }

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (flagRetorno)
                    flagRetorno = false
                else {
                    viewModel.listarProducto(p0.toString().trim())
                }
            }

        })

        binding.fabCarrito.setOnClickListener {
            if (viewModel.listaCarrito.value?.size!! > 0) {
                flagRetorno = true
                Navigation.findNavController(it)
                    .navigate(R.id.action_nav_home_to_registrarPedidoFragment)
            } else {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA",
                    "No existe elementos en el carrito",
                    it.context
                )
            }
        }

        // Muestra la lista
        if (viewModel.listaProducto.value == null)
            viewModel.listarProducto(binding.etBuscar.text.toString().trim())
    }

    companion object {
        private var flagRetorno = false
        private var flagCantidad = false
    }

    override fun clickItem(entidad: ProductoModel) {
        UtilsCommon.ocultarTeclado(requireView())

        if (!flagCantidad) {
            flagCantidad = true
            viewModel.setItemProducto(entidad)

            CantidadDialog.newInstance(
                "",
                "Selecc. cantidad y precio",
                entidad.precio,
                this
            ).show(parentFragmentManager, "Cantidad")
        }
    }

    override fun enviarItem(cantidad: Int, precio: Double) {
        flagCantidad = false
        if (cantidad == 0 || precio == 0.0) return

        for (det in viewModel.listaCarrito.value!!) {
            if (det.idproducto == viewModel.itemProducto.value?.id) {
                Toast.makeText(context, "Ya existe este elemento en su lista...", Toast.LENGTH_LONG)
                    .show()
                viewModel.setItemProducto(null)
                return
            }
        }

        if (viewModel.itemProducto.value == null) return

        val entidad = DetallePedidoModel().apply {
            idproducto = viewModel.itemProducto.value!!.id
            this.descripcion = viewModel.itemProducto.value!!.descripcion
            this.cantidad = cantidad
            this.precio = precio
            this.importe = cantidad * precio
        }

        viewModel.agregarProductoCarrito(entidad)
        viewModel.setItemProducto(null)
    }
}