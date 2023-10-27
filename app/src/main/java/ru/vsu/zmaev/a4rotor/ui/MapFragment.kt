package ru.vsu.zmaev.a4rotor.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import ru.vsu.zmaev.a4rotor.BuildConfig
import ru.vsu.zmaev.a4rotor.R
import ru.vsu.zmaev.a4rotor.data.model.point.PointData
import ru.vsu.zmaev.a4rotor.data.network.PointRequestBody
import ru.vsu.zmaev.a4rotor.databinding.FragmentMapBinding
import ru.vsu.zmaev.a4rotor.ui.map.viewmodel.MapViewModel

class MapFragment : Fragment(), CameraListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MapViewModel>()

    private lateinit var mapObjectCollection: MapObjectCollection // Коллекция различных объектов на карте
    private lateinit var placemarkMapObject: PlacemarkMapObject

    private val mapObjectTapListener = MapObjectTapListener { mapObject, point ->
        true
    }

    private var zoomValue: Float = 16.5f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setApiKey(savedInstanceState)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trackButton.setOnClickListener {
            viewModel.getPoint(PointRequestBody(binding.trackNumberEt.text.toString()))
        }
        viewModel.point.observe(viewLifecycleOwner) {
            setMarkerToLocation(it);
        }
    }


    private fun setMarkerToLocation(point: PointData) {
        val marker = createBitmapFromVector(R.drawable.ic_drone)
        mapObjectCollection = binding.mapview.map.mapObjects
        placemarkMapObject =
            mapObjectCollection.addPlacemark(Point(point.locationX, point.locationY), ImageProvider.fromBitmap(marker))
        placemarkMapObject.opacity = 0.5f
        placemarkMapObject.addTapListener(mapObjectTapListener)
    }
    private fun setApiKey(savedInstanceState: Bundle?) {
        val haveApiKey = savedInstanceState?.getBoolean("haveApiKey") ?: false
        if (!haveApiKey) {
            MapKitFactory.setApiKey(BuildConfig.API_KEY)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finished: Boolean
    ) {
        if (finished) {
            when {
                cameraPosition.zoom >= ZOOM_BOUNDARY && zoomValue <= ZOOM_BOUNDARY -> {
                    placemarkMapObject.setIcon(ImageProvider.fromBitmap(createBitmapFromVector(R.drawable.ic_pin_blue_svg)))
                }
                cameraPosition.zoom <= ZOOM_BOUNDARY && zoomValue >= ZOOM_BOUNDARY -> {
                    placemarkMapObject.setIcon(ImageProvider.fromBitmap(createBitmapFromVector(R.drawable.ic_pin_red_svg)))
                }
            }
            zoomValue = cameraPosition.zoom
        }
    }

    private fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(requireContext(), art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    companion object {
        const val ZOOM_BOUNDARY = 16.4f
    }
}