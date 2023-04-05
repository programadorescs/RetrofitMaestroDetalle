package pe.pcs.retrofitmaestrodetalle.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.retrofitmaestrodetalle.core.UtilsCommon
import pe.pcs.retrofitmaestrodetalle.core.UtilsMessage
import pe.pcs.retrofitmaestrodetalle.databinding.FragmentReporteDetallePedidoBinding
import pe.pcs.retrofitmaestrodetalle.ui.adapter.ReporteDetallePedidoAdapter
import pe.pcs.retrofitmaestrodetalle.ui.viewmodel.ReportePedidoViewModel

@AndroidEntryPoint
class ReporteDetallePedidoFragment : Fragment() {

    private lateinit var binding: FragmentReporteDetallePedidoBinding
    private val viewModel: ReportePedidoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReporteDetallePedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        viewModel.listaDetalle.observe(viewLifecycleOwner) {
            binding.rvLista.adapter = ReporteDetallePedidoAdapter(it)
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

        viewModel.itemPedido.observe(viewLifecycleOwner) {
            if (viewModel.itemPedido.value != null) {
                binding.tvPedido.text = "Pedido: ${viewModel.itemPedido.value?.id ?: 0}"
                binding.tvImporte.text =
                    UtilsCommon.formatearDoubleString(viewModel.itemPedido.value?.total ?: 0.0)
            }
        }

        viewModel.listarDetalle(viewModel.itemPedido.value?.id ?: 0)
    }
}