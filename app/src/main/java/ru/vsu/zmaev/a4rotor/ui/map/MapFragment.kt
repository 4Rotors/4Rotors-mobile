package ru.vsu.zmaev.a4rotor.ui.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import ru.vsu.zmaev.a4rotor.BuildConfig
import ru.vsu.zmaev.a4rotor.R
import ru.vsu.zmaev.a4rotor.model.PointData
import ru.vsu.zmaev.a4rotor.network.PointApi
import ru.vsu.zmaev.a4rotor.model.PointRequestBody
import ru.vsu.zmaev.a4rotor.repository.MainRepositoryImpl
import ru.vsu.zmaev.a4rotor.databinding.FragmentMapBinding
import ru.vsu.zmaev.a4rotor.factory.CustomViewModelFactory
import ru.vsu.zmaev.a4rotor.ui.map.viewmodel.MapViewModel

class MapFragment : Fragment(), CameraListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MapViewModel

    private lateinit var userLocationLayer: UserLocationLayer

    private var routeStartLocation = Point(0.0, 0.0)

    private var followUserLocation = false

    private lateinit var placemarkMapObject: PlacemarkMapObject
    private lateinit var mapObjectCollection: MapObjectCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        setApiKey(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val api = PointApi.getPointApi()!!
        val repository = MainRepositoryImpl(api)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            CustomViewModelFactory(repository),
        )[MapViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(binding.mapView.mapWindow)
        mapObjectCollection = binding.mapView.map.mapObjects
        placemarkMapObject = mapObjectCollection.addEmptyPlacemark(Point())
        binding.trackButton.setOnClickListener {
            val pointRequestBody = PointRequestBody(binding.trackNumberEt.text.toString())
            viewModel.getPoint(pointRequestBody)
        }
        viewModel.point.observe(viewLifecycleOwner) {
            mapObjectCollection.remove(placemarkMapObject)
            cameraDronePosition(it)
        }
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    private fun cameraDronePosition(point: PointData) {
        val marker = createBitmapFromVector(R.drawable.ic_drone)
        routeStartLocation = Point(point.locationX, point.locationY)
        placemarkMapObject = mapObjectCollection.addPlacemark(Point(point.locationX, point.locationY), ImageProvider.fromBitmap(marker))
        binding.mapView.map.move(
            CameraPosition(routeStartLocation, 16f, 0f, 0f), Animation(Animation.Type.SMOOTH, 1f), null
        )
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
        map: Map, cPos: CameraPosition, cUpd: CameraUpdateReason, finish: Boolean
    ) {
        if (finish) {
            if (followUserLocation) {
                setAnchor()
            }
        } else {
            if (!followUserLocation) {
                noAnchor()
            }
        }
    }

    private fun noAnchor() {
        userLocationLayer.resetAnchor()

    }

    private fun setAnchor() {
        userLocationLayer.setAnchor(
            PointF(
                (binding.mapView.width * 0.5).toFloat(), (binding.mapView.height * 0.5).toFloat()
            ),
            PointF(
                (binding.mapView.width * 0.5).toFloat(), (binding.mapView.height * 0.83).toFloat()
            )
        )
        followUserLocation = false
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