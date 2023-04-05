package pe.pcs.retrofitmaestrodetalle.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.retrofitmaestrodetalle.core.UtilsCommon
import pe.pcs.retrofitmaestrodetalle.core.UtilsMessage
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.databinding.FragmentOperacionProductoBinding
import pe.pcs.retrofitmaestrodetalle.ui.viewmodel.ProductoViewModel

@AndroidEntryPoint
class OperacionProductoFragment : Fragment() {

    private lateinit var binding: FragmentOperacionProductoBinding
    private val viewModel: ProductoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOperacionProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.item.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.etDescripcion.setText(it.descripcion)
                binding.etCosto.setText(UtilsCommon.formatearDoubleString(it.costo))
                binding.etPrecio.setText(UtilsCommon.formatearDoubleString(it.precio))
            }
        }

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
            if (it == null) return@observe

            if (it.isSuccess) {
                UtilsMessage.showToast(it.message)
                UtilsCommon.limpiarEditText(requireView())
                binding.etDescripcion.requestFocus()
                viewModel.setItem(null)
            } else
                UtilsMessage.showAlertOk("ADVERTENCIA", it.message, requireContext())

            viewModel.operacionExitosa.postValue(null)
        }

        binding.fabGrabar.setOnClickListener {
            UtilsCommon.ocultarTeclado(it)

            if (binding.etDescripcion.text.toString().trim().isEmpty() ||
                binding.etCosto.text.toString().trim().isEmpty() ||
                binding.etPrecio.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showToast("Todos los datos son requeridos.")
                return@setOnClickListener
            }

            val entidad = ProductoModel().apply {
                id = viewModel.item.value?.id ?: 0
                descripcion = binding.etDescripcion.text.toString()
                costo = binding.etCosto.text.toString().toDouble()
                precio = binding.etPrecio.text.toString().toDouble()
            }

            viewModel.grabar(entidad)
        }
    }
}