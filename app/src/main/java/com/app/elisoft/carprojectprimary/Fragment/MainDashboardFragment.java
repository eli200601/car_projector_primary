package com.app.elisoft.carprojectprimary.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.elisoft.carprojectprimary.Activity.DeviceListActivity;
import com.app.elisoft.carprojectprimary.BluetoothUtils.BluetoothChatService;
import com.app.elisoft.carprojectprimary.BluetoothUtils.Constants;
import com.app.elisoft.carprojectprimary.R;
import com.app.elisoft.carprojectprimary.Recycler.RecyclerAdapter;
import com.app.elisoft.carprojectprimary.Recycler.RecyclerCallback;
import com.app.elisoft.carprojectprimary.Recycler.SignItem;
import com.app.elisoft.carprojectprimary.Utils.Helper;
import com.app.elisoft.carprojectprimary.Utils.Keys;
import com.app.elisoft.carprojectprimary.Utils.SharedPrefsUtils;

import java.util.ArrayList;


public class MainDashboardFragment extends Fragment {
    private static final String TAG="MainDashboardFragment";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<SignItem> signsListArray;

    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    private boolean isSignOn = false;
    private ImageView projectorImageSign;
    private ImageView alertLampOne, alertLampTwo;
    AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
    AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Activity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
        createSignsListArray();
    }

    public void createSignsListArray(){
        signsListArray = new ArrayList<>();

        signsListArray.add(new SignItem("kids", R.drawable.kids));
        signsListArray.add(new SignItem("alert", R.drawable.alert));
        signsListArray.add(new SignItem("accident", R.drawable.accident));
        signsListArray.add(new SignItem("alert_yellow", R.drawable.alert_yellow));
        signsListArray.add(new SignItem("disabled", R.drawable.disabled));
        signsListArray.add(new SignItem("school", R.drawable.school));
        signsListArray.add(new SignItem("work", R.drawable.work));

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            Log.d(TAG, "mChatService is null, need to create new");
//            setupChat();
            mChatService = new BluetoothChatService(getActivity(), mHandler);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String viewType = SharedPrefsUtils.getStringPreference(getActivity().getApplicationContext(), Keys.PREF_VIEW_TYPE);
        View view = null;
        Log.d(TAG, "viewType = " + viewType);
        switch (viewType) {
            case Keys.VIEW_DASHBOARD: {
                Log.d(TAG, "Creating Dashboard view");
                view = inflater.inflate(R.layout.fragment_dashboard, container, false);
//                setHasOptionsMenu(true);
                mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Keys.NUMBER_OF_COLUMNS));
                mRecyclerView.setHasFixedSize(true);
                mAdapter = new RecyclerAdapter(getActivity().getApplicationContext(), signsListArray);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setRecyclerCallback(mCallback);
                mAdapter.notifyDataSetChanged();
                break;
            }
            case Keys.VIEW_PROJECTOR: {
                //Todo: fill in please
                Log.d(TAG, "Creating Projector view");



                view = inflater.inflate(R.layout.fragment_projector, container, false);
                projectorImageSign = (ImageView) view.findViewById(R.id.main_sign);
                alertLampOne = (ImageView) view.findViewById(R.id.lamp_one);
                alertLampTwo = (ImageView) view.findViewById(R.id.lamp_two);
                projectorImageSign.setVisibility(View.INVISIBLE);
                break;
            }

        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        mSendButton = (Button) view.findViewById(R.id.button_send);
//        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
//        mConversationView = (ListView) view.findViewById(R.id.in);

    }

    private RecyclerCallback mCallback = new RecyclerCallback() {
        @Override
        public void sendActionToProjector(SignItem item) {
            //Need to send message to projector...
            Log.d(TAG, "Item clicked: " + item.getName());
            String message = item.getName();
            sendMessage(message);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);

//                    String address = data.getExtras()
//                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//                    Log.d(TAG, "REQUEST_CONNECT_DEVICE_SECURE Device Connected with: ..." + device);
//                    mChatService.connect(device, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
//                    connectDevice(data, false);
                    Log.d(TAG, "Device Connected with: ...");
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
//                    setupChat();
                    mChatService = new BluetoothChatService(getActivity(), mHandler);
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        Log.d(TAG, address);
        if (mChatService == null) {
            Log.d(TAG, "mChatService is null ");
        }else {
            mChatService.connect(device, secure);
        }

    }

    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
//            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer);
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
//            case R.id.insecure_connect_scan: {
//                // Launch the DeviceListActivity to see devices and do scan
//                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
//                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
//                return true;
//            }
//            case R.id.discoverable: {
//                // Ensure this device is discoverable by others
//                ensureDiscoverable();
//                return true;
//            }
        }
        return false;
    }

    public int getImageResourceFromString(String message) {
        int id = 0;
        for (SignItem item: signsListArray) {
            if (item.getName().equals(message)) id = item.getImage();
        }
        return id;
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
//                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
//                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
//                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
//                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d(TAG, "Message got: " + readMessage);
                    //TODO: hare i got messages....
                    if (isSignOn) {
                        isSignOn = false;
                        projectorImageSign.setVisibility(View.GONE);
                        fadeIn.cancel();
                        fadeIn.reset();
                        fadeOut.cancel();
                        fadeOut.reset();
                        alertLampOne.setVisibility(View.INVISIBLE);
                        alertLampTwo.setVisibility(View.INVISIBLE);
                    } else {
                        isSignOn = true;
                        projectorImageSign.setImageBitmap(Helper.decodeSampledBitmapFromResource(getResources(), getImageResourceFromString(readMessage), 400, 400));
                        projectorImageSign.setVisibility(View.VISIBLE);

                        fadeIn.setRepeatCount(Animation.INFINITE);
                        fadeOut.setRepeatCount(Animation.INFINITE);
                        fadeIn.setDuration(500);
                        fadeOut.setDuration(500);
                        fadeOut.setStartOffset(300+fadeIn.getStartOffset()+300);
                        alertLampOne.startAnimation(fadeIn);
                        alertLampOne.startAnimation(fadeOut);
                        alertLampTwo.startAnimation(fadeIn);
                        alertLampTwo.startAnimation(fadeOut);

                    }

//                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
