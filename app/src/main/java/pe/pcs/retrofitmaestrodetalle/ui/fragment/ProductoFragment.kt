package pe.pcs.retrofitmaestrodetalle.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.retrofitmaestrodetalle.R
import pe.pcs.retrofitmaestrodetalle.core.UtilsCommon
import pe.pcs.retrofitmaestrodetalle.core.UtilsMessage
import pe.pcs.retrofitmaestrodetalle.data.model.ProductoModel
import pe.pcs.retrofitmaestrodetalle.databinding.FragmentProductoBinding
import pe.pcs.retrofitmaestrodetalle.ui.adapter.ProductoAdapter
import pe.pcs.retrofitmaestrodetalle.ui.viewmodel.ProductoViewModel

@AndroidEntryPoint
class ProductoFragment : Fragment(), ProductoAdapter.IOnClickListener {

    private lateinit var binding: FragmentProductoBinding
    private val viewModel: ProductoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        binding.rvLista.adapter = ProductoAdapter(this)

        viewModel.lista.observe(viewLifecycleOwner) {
            (binding.rvLista.adapter as ProductoAdapter).submitList(it)
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
            if(it == null) return@observe

            if (it.isSuccess)
                UtilsMessage.showToast(it.message)
            else
                UtilsMessage.showAlertOk("ADVERTENCIA", it.message, requireContext())

            viewModel.operacionExitosa.postValue(null)
        }

        binding.fabNuevo.setOnClickListener {
            flagRetorno = true
            viewModel.setItem(null)
            Navigation.findNavController(it).navigate(R.id.action_nav_producto_to_operacionProductoFragment)
        }

        binding.tilBuscar.setEndIconOnClickListener {
            UtilsCommon.ocultarTeclado(it)
            binding.etBuscar.setText("")
            viewModel.listar("")
        }

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (flagRetorno)
                    flagRetorno = false
                else {
                    viewModel.listar(p0.toString().trim())
                }
            }

        })

        if(viewModel.lista.value == null)
            viewModel.listar("")
    }

    companion object {
        private var flagRetorno = false
    }

    override fun clickEditar(entidad: ProductoModel) {
        flagRetorno = true
        viewModel.setItem(entidad)
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_producto_to_operacionProductoFragment)
    }

    override fun clickEliminar(entidad: ProductoModel) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setTitle("ELIMINAR")
            setMessage("¿Desea eliminar el registro: ${entidad.descripcion}")

            setPositiveButton("SI") { dialog, _ ->
                viewModel.eliminar(entidad.id.toLong())
                dialog.dismiss()
            }

            setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }
}