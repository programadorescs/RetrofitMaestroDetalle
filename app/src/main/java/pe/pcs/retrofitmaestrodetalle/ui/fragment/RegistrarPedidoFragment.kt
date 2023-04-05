package pe.pcs.retrofitmaestrodetalle.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.retrofitmaestrodetalle.core.UtilsAdmob
import pe.pcs.retrofitmaestrodetalle.core.UtilsCommon
import pe.pcs.retrofitmaestrodetalle.core.UtilsDate
import pe.pcs.retrofitmaestrodetalle.core.UtilsMessage
import pe.pcs.retrofitmaestrodetalle.data.model.DetallePedidoModel
import pe.pcs.retrofitmaestrodetalle.data.model.PedidoModel
import pe.pcs.retrofitmaestrodetalle.databinding.FragmentRegistrarPedidoBinding
import pe.pcs.retrofitmaestrodetalle.ui.adapter.CarritoAdapter
import pe.pcs.retrofitmaestrodetalle.ui.viewmodel.PedidoViewModel

@AndroidEntryPoint
class RegistrarPedidoFragment : Fragment(), CarritoAdapter.IOnClickListener {

    private lateinit var binding: FragmentRegistrarPedidoBinding
    private val viewModel: PedidoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrarPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        binding.rvLista.adapter = CarritoAdapter(this)

        viewModel.progressBar.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        viewModel.mErrorStatus.observe(viewLifecycleOwner) {
            if(it.isNullOrEmpty()) return@observe

            UtilsMessage.showAlertOk(
                "ERROR", it, requireContext()
            )

            viewModel.mErrorStatus.postValue("")
        }

        viewModel.operacionExitosa.observe(viewLifecycleOwner) {
            if(it != null) {
                if (it.isSuccess) {
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setTitle("CONFORME")
                        setMessage("¡El pedido fue registrado correctamente!")
                        setCancelable(false)
                        setPositiveButton("Aceptar") { dialog, _ ->

                            // Mostrar anuncio
                            UtilsAdmob.interstitial?.show(requireActivity())
                            // Carga un nuevo anuncio para el siguiente click
                            UtilsAdmob.initInterstitial()

                            viewModel.limpiarCarrito()

                            binding.etCliente.setText("")

                            Navigation.findNavController(requireView()).popBackStack()
                            dialog.cancel()
                        }
                    }.create().show()
                } else
                    UtilsMessage.showAlertOk("ADVERTENCIA", it.message, requireContext())

                viewModel.operacionExitosa.postValue(null)
            }
        }

        viewModel.listaCarrito.observe(viewLifecycleOwner) {
            (binding.rvLista.adapter as CarritoAdapter).setData(it)
        }

        viewModel.totalImporte.observe(viewLifecycleOwner) {
            binding.tvImporte.text = UtilsCommon.formatearDoubleString(it)
        }

        binding.fabCarrito.setOnClickListener {
            UtilsCommon.ocultarTeclado(it)

            if(!viewModel.listaCarrito.value.isNullOrEmpty()) {
                val pedido = PedidoModel().apply {
                    fecha = UtilsDate.obtenerFechaActual()
                    total = viewModel.totalImporte.value!!
                    cliente = binding.etCliente.text.toString().trim().ifEmpty { "Publico general" }
                    estado = "vigente"
                    detalles = viewModel.listaCarrito.value
                }

                viewModel.registrar(pedido)
            } else {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA",
                    "No exsite items en el carrito",
                    requireContext())
            }
        }

        viewModel.listarCarrito()
    }

    override fun clickMas(entidad: DetallePedidoModel) {
        viewModel.setAumentarCantidadProducto(entidad)
    }

    override fun clickMenos(entidad: DetallePedidoModel) {
        viewModel.setDisminuirCantidadProducto(entidad)
    }

    override fun clickEliminar(entidad: DetallePedidoModel) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setTitle("QUITAR")
            setMessage("¿Desea quitar el registro: ${entidad.descripcion}?")

            setPositiveButton("SI") {dialog, _ ->
                viewModel.quitarProductoCarrito(entidad)

                if(viewModel.listaCarrito.value?.size!! == 0) {
                    binding.etCliente.setText("")
                    Navigation.findNavController(requireView()).popBackStack()
                }

                dialog.dismiss()
            }

            setNegativeButton("NO"){ dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }
}