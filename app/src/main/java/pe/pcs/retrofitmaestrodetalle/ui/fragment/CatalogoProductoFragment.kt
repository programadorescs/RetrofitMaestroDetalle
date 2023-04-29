package pe.pcs.retrofitmaestrodetalle.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.retrofitmaestrodetalle.R
import pe.pcs.retrofitmaestrodetalle.core.UtilsCommon
import pe.pcs.retrofitmaestrodetalle.core.UtilsMessage
import pe.pcs.retrofitmaestrodetalle.databinding.FragmentCatalogoProductoBinding
import pe.pcs.retrofitmaestrodetalle.core.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.domain.model.Producto
import pe.pcs.retrofitmaestrodetalle.ui.adapter.CatalogoAdapter
import pe.pcs.retrofitmaestrodetalle.ui.dialog.CantidadDialog
import pe.pcs.retrofitmaestrodetalle.ui.viewmodel.PedidoViewModel

@AndroidEntryPoint
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
            (binding.rvLista.adapter as CatalogoAdapter).submitList(it)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Error -> {
                    binding.progressBar.isVisible = false

                    if (it.message.isNotEmpty())
                        UtilsMessage.showAlertOk(
                            "ERROR", it.message, requireContext()
                        )

                    viewModel.resetApiResponseStatus()
                }

                is ResponseStatus.Loading -> binding.progressBar.isVisible = true
                is ResponseStatus.Success -> binding.progressBar.isVisible = false
                else -> Unit
            }
        }

        viewModel.mErrorStatus.observe(viewLifecycleOwner) {
            if(it.isNullOrEmpty()) return@observe

            UtilsMessage.showAlertOk(
                "ERROR", it, requireContext()
            )

            viewModel.mErrorStatus.postValue("")
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

    override fun clickItem(entidad: Producto) {
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

        viewModel.agregarProductoCarrito(cantidad, precio)
    }
}