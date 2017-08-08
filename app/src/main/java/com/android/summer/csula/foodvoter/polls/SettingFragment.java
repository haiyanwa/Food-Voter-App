package com.android.summer.csula.foodvoter.polls;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.summer.csula.foodvoter.R;
import com.android.summer.csula.foodvoter.polls.models.Poll;
import com.android.summer.csula.foodvoter.yelpApi.models.Coordinate;
import com.android.summer.csula.foodvoter.yelpApi.models.YelpPriceLevel;

public class SettingFragment extends Fragment {

    private static final String TAG = SettingFragment.class.getSimpleName();

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ZIP_CODE = "zipCode";
    private static final String KEY_PRICE = "price";
    private static final String KEY_OPEN = "openNow";

    private boolean hasLocation;
    private Spinner priceSpinner;
    private EditText zipCode;
    private RadioButton currentLocationRadioButton;
    private RadioButton zipCodeRadioButton;
    private View view;

    private OnPollSettingsListener onPollSettingsListener;
    private RadioGroup locationRadioGroup;

    public SettingFragment() {}


    public static SettingFragment newInstance(Poll poll) {
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(buildPollBundle(poll));
        return fragment;
    }

    private static Bundle buildPollBundle(Poll poll) {
        Bundle args = new Bundle();

        args.putString(KEY_TITLE, poll.getTitle());
        args.putString(KEY_DESCRIPTION, poll.getDescription());
        args.putString(KEY_ZIP_CODE, poll.getZipCode());
        args.putString(KEY_PRICE, poll.getPrice());
        args.putBoolean(KEY_OPEN, poll.isOpenNow());
        return args;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.setting_fragment, container, false);

        initializeUI();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Activity MUST implement this interface because it is used to communicated between
        // this fragment and the host activity.
        try {
            onPollSettingsListener = (OnPollSettingsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("context must implement OnPollListener");
        }
    }

    /**
     * Allow Host Activity to tell this Fragment weather or not to unlock the "current_location"
     * button. If the app have access to the user's location, then this should be set to true.
     * When false, the user is required to enter their location manually.
     * */
    public void unlockCurrentLocationRadio(boolean value) {
        this.hasLocation = value;
        updateButtonState();
        setupRadioGroups();
    }

    private void initializeUI() {
        setupTitle();
        setupDescription();
        setupZipCode();
        setupCheckbox();
        setupSpinner();
        setupRadioGroups();
        setupRadioButtons();

    }

    private void setupRadioButtons() {
        // If our activity tell us that it can't find a location (happens alot with emulator)
        // then disable the location radio button
        currentLocationRadioButton = (RadioButton) view.findViewById(R.id.radio_button_current_location);
        zipCodeRadioButton = (RadioButton) view.findViewById(R.id.radio_button_use_zip_code);

        updateButtonState();
    }

    private void updateButtonState() {
        currentLocationRadioButton.setEnabled(hasLocation);
        currentLocationRadioButton.setChecked(hasLocation);
        zipCodeRadioButton.setChecked(!hasLocation);
    }

    private void setupCheckbox() {
        CheckBox openNow = (CheckBox) view.findViewById(R.id.checkbox_open_now);
        boolean pollOpenNow = getArguments().getBoolean(KEY_OPEN, false);
        openNow.setChecked(pollOpenNow);

        openNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onPollSettingsListener.onOpenNowChange(b);
            }
        });
    }

    private void setupZipCode() {
        zipCode = (EditText) view.findViewById(R.id.edit_text_zip_code);
        String pollZipCode = getArguments().getString(KEY_ZIP_CODE);
        zipCode.setText(pollZipCode);

        zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onPollSettingsListener.onZipCodeChange(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void setupDescription() {
        EditText description = (EditText) view.findViewById(R.id.edit_text_description);
        String pollDescription = getArguments().getString(KEY_DESCRIPTION);
        description.setText(pollDescription);

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onPollSettingsListener.onDescriptionChange(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void setupTitle() {
        EditText title = (EditText) view.findViewById(R.id.edit_text_title);
        String pollTitle = getArguments().getString(KEY_TITLE);
        title.setText(pollTitle);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onPollSettingsListener.onTitleChange(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void setupRadioGroups() {
        locationRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        locationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (R.id.radio_button_use_zip_code == i) {
                    zipCode.setEnabled(true);
                    onPollSettingsListener.onUseCoordinate(false);
                } else if (R.id.radio_button_current_location == i && hasLocation) {
                    zipCode.getText().clear();
                    zipCode.setEnabled(false);
                    onPollSettingsListener.onUseCoordinate(true);
                }
            }
        });
    }

    private void setupSpinner() {
        priceSpinner = (Spinner) view.findViewById(R.id.spinners_price);

        /* From https://developer.android.com/guide/topics/ui/controls/spinner.html */

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.price_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        priceSpinner.setAdapter(adapter);

        // Update the spinner position
        setSpinnerPosition();

        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();
                onPollSettingsListener.onPriceChange(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void setSpinnerPosition() {
        String pollPrice = getArguments().getString(KEY_PRICE);
        String yelpPollPrice = YelpPriceLevel.toYelpString(pollPrice);
        int position = getSpinnerPosition(yelpPollPrice);
        priceSpinner.setSelection(position);
    }

    /**
     * Return the spinner position of a yelp price level ($, $$, $$$, $$$$). Defaults to 0 ($)
     * if none are found (for some reason).
     */
    private int getSpinnerPosition(String yelpPriceLevel) {
        for (int i = 0; i < priceSpinner.getCount(); i++) {
            String item = (String) priceSpinner.getItemAtPosition(i);
            if (yelpPriceLevel != null && yelpPriceLevel.equals(item)) {
                return i;
            }
        }
        // 0 is the $ in the spinner, which is the default price in the Poll class
        return 0;
    }

    /**
     * This interface is use to let the host activity know when a setting field has change in the
     * setting tab
     */
    interface OnPollSettingsListener {
        void onTitleChange(String title);

        void onDescriptionChange(String description);

        void onOpenNowChange(boolean openNow);

        void onPriceChange(String price);

        void onZipCodeChange(String zipCode);

        void onUseCoordinate(boolean useCoordinate);
    }
}
