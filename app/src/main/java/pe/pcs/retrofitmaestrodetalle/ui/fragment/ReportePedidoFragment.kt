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
import pe.pcs.retrofitmaestrodetalle.ui.utils.ResponseStatus
import pe.pcs.retrofitmaestrodetalle.ui.utils.UtilsDate
import pe.pcs.retrofitmaestrodetalle.ui.utils.UtilsMessage
import pe.pcs.retrofitmaestrodetalle.databinding.FragmentReportePedidoBinding
import pe.pcs.retrofitmaestrodetalle.domain.model.Pedido
import pe.pcs.retrofitmaestrodetalle.ui.adapter.ReportePedidoAdapter
import pe.pcs.retrofitmaestrodetalle.ui.viewmodel.ReportePedidoViewModel

@AndroidEntryPoint
class ReportePedidoFragment : Fragment(), ReportePedidoAdapter.IOnClickListener {

    private lateinit var binding: FragmentReportePedidoBinding
    private val viewModel: ReportePedidoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportePedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        binding.rvLista.adapter = ReportePedidoAdapter(this)

        viewModel.listaPedido.observe(viewLifecycleOwner) {
            (binding.rvLista.adapter as ReportePedidoAdapter).submitList(it)
        }

        viewModel.statePedido.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Error -> {
                    binding.progressBar.isVisible = false

                    if (it.message.isNotEmpty())
                        UtilsMessage.showAlertOk(
                            "ERROR", it.message, requireContext()
                        )
                }

                is ResponseStatus.Loading -> binding.progressBar.isVisible = true
                is ResponseStatus.Success -> binding.progressBar.isVisible = false
            }
        }

        viewModel.stateAnularPedido.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Error -> {
                    binding.progressBar.isVisible = false

                    if (it.message.isNotEmpty())
                        UtilsMessage.showAlertOk(
                            "ERROR", it.message, requireContext()
                        )
                }

                is ResponseStatus.Loading -> binding.progressBar.isVisible = true
                is ResponseStatus.Success -> {
                    binding.progressBar.isVisible = false

                    if (it.data > 0) {
                        UtilsMessage.showToast("¡Felicidades, registro anulado correctamente!")
                        viewModel.resetStateAnularPedido()
                    }
                }
            }
        }

        binding.etDesde.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etDesde, parentFragmentManager)
        }

        binding.etHasta.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etHasta, parentFragmentManager)
        }

        binding.etDesde.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                buscarPedido()
            }
        })

        binding.etHasta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                buscarPedido()
            }
        })

        if (!flagRetorno) {
            UtilsDate.mostrarFechaActual(binding.etDesde)
            UtilsDate.mostrarFechaActual(binding.etHasta)
        }
    }

    companion object {
        private var flagRetorno = false
    }

    private fun buscarPedido() {
        if (binding.etDesde.text.toString().isEmpty() ||
            binding.etHasta.text.toString().isEmpty()
        ) return

        if (flagRetorno)
            flagRetorno = false
        else
            viewModel.listarPedido(
                binding.etDesde.text.toString(),
                binding.etHasta.text.toString()
            )
    }

    override fun clickAnular(entidad: Pedido) {
        if (entidad.estado.trim().lowercase() == "anulado") {
            UtilsMessage.showToast("El pedido ya esta anulado")
            return
        }

        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setTitle("ANULAR PEDIDO")
            setMessage("¿Esta seguro de querer anular el Pedido: ${entidad.id}?")

            setPositiveButton("SI") { dialog, _ ->
                viewModel.anularPedido(
                    entidad.id,
                    binding.etDesde.text.toString(),
                    binding.etHasta.text.toString()
                )
                dialog.dismiss()
            }

            setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    override fun clickDetalle(entidad: Pedido) {
        flagRetorno = true
        viewModel.setItem(entidad)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_nav_reporte_pedido_to_reporteDetallePedidoFragment)
    }
}