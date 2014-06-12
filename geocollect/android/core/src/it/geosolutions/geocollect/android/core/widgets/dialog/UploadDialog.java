package it.geosolutions.geocollect.android.core.widgets.dialog;

import it.geosolutions.geocollect.android.core.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * Fragment that shows uplaod status
 * @author Lorenzo Natali (lorenzo.natali@geo-solutions.it)
 *
 */
public class UploadDialog extends RetainedDialogFragment {

	private ProgressBar dataProgress;
	private ProgressBar progressMedia;
	private TextView txtDataSend;
	private TextView txtMediaSend;
	private ImageView imgOKData;
	private ImageView imgOKMedia;
	private ImageView imgBadData;
	private ImageView imgBadMedia;
	private boolean skipData = false;
	private Activity activity;
	private static boolean sending = false;

	public UploadDialog() {
		// Empty constructor required for DialogFragment
		super();

	}
	/**
	 * Create the view that display current upload progress 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			//TODO the retained fragment call this method.
			if(getView()==null){;
				View view = inflater.inflate(R.layout.progress_send, container);
				getDialog().setTitle(getString(R.string.sending_data));
				getDialog().setCancelable(false);
				//if rotation continue to have problems, block orientation change.
				//don't forget to remove the requested orientation after finish
				//getActivity().setRequestedOrientation(getActivity().getResources().getConfiguration().orientation);
				return view;
			} else return getView();
			
		
			//return super.onCreateView( inflater,  container,  savedInstanceState);
		

		
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if( sending == false ){
			setupControls();
		}
	}

	
	/**
	 * setup view components (progress elements and textviews for data and progress)
	 */
	protected void setupControls() {
		dataProgress = (ProgressBar) getView().findViewById(
				R.id.progress_data_send);

		progressMedia = (ProgressBar) getView().findViewById(
				R.id.progress_media_send);

		txtDataSend = (TextView) getView().findViewById(
				R.id.txt_data_send);

		txtMediaSend = (TextView) getView().findViewById(
				R.id.txt_media_send);

		imgOKData = (ImageView) getView().findViewById(
				R.id.img_data_send_ok);
		imgBadData = (ImageView) getView().findViewById(
				R.id.img_data_send_bad);

		imgOKMedia = (ImageView) getView()
				.findViewById(R.id.img_media_send_ok);
		imgBadMedia = (ImageView) getView().findViewById(
				R.id.img_media_send_bad);

	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		if (isAdded() && (!sending)) {
			new SendDataThread().execute();
		}
	}
	public class Result{}

	/**
	 * Dummy thread 
	 * 
	 * @author Lorenzo Natali (lorenzo.natali@geo-solutions.it)
	 * 
	 */
	private class SendDataThread extends
			AsyncTask<Void, Void, Result> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			sending = true;
	
				setupDataControls(true);
			

		}

		@Override
		protected Result doInBackground(Void... params) {
			Result result = null;

			if (!skipData) {
				
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
					Log.e("SendData CHIUS", Log.getStackTraceString(e));

				}

			}

			return result;
		}

		@Override
		protected void onPostExecute(Result result) {
			super.onPostExecute(result);
			setupDataControls(false);
			setDataSendResultUI(true);
			new MediaSenderThread().execute();

		}

	}

	/**
	 * Send Media data dummy Thread
	 * 
	 * @author Lorenzo Natali (lorenzo.natali@geo-solutions.it)
	 * 
	 */
	private class MediaSenderThread extends AsyncTask<Void, Void, Result> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

				setupMediaControl(true);
			

		}

		@Override
		protected Result doInBackground(Void... params) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new Result();
		}

		@Override
		protected void onPostExecute(Result result) {
			setupMediaControl(false);
			setMediaSendResultUI(true);
			closeDialog(true);
			Activity c = activity != null ? activity : getActivity();
			onFinish(c,result);
		}


		

	}


	/**
	 * CloseDialog
	 * 
	 * @param closeActivity
	 */
	private void closeDialog(boolean closeActivity) {

		//getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		this.dismiss();
		sending = false;
		
	}

	

	/**
	 * Method to override called when application finish
	 * @param activity the current running activity
	 * @param result
	 */
	public void onFinish(Activity ctx, Result result){
		
	}
	/**
	 * setup the controls for data upload
	 * 
	 * @param started
	 */
	private void setupDataControls(boolean started) {
		
		if (started) {
			//display progress
			dataProgress.setVisibility(View.VISIBLE);
			txtDataSend.setTypeface(null, Typeface.BOLD_ITALIC);
		} else {
			//hide progress
			dataProgress.setVisibility(View.GONE);
			txtDataSend.setTypeface(null, Typeface.NORMAL);
		}
	}

	/**
	 * setup the controls for media upload
	 * 
	 * @param started
	 */
	private void setupMediaControl(boolean started) {
		if (started) {
			//display progress
			progressMedia.setVisibility(View.VISIBLE);
			txtMediaSend.setTypeface(null, Typeface.BOLD_ITALIC);
		} else {
			//hide progress
			progressMedia.setVisibility(View.GONE);
			txtMediaSend.setTypeface(null, Typeface.NORMAL);
		}
	}

	/**
	 * set
	 * 
	 * @param set up result for data send
	 */
	private void setDataSendResultUI(boolean success) {
		if (success) {
			imgBadData.setVisibility(View.GONE);
			imgOKData.setVisibility(View.VISIBLE);
		} else {
			imgBadData.setVisibility(View.VISIBLE);
			imgOKData.setVisibility(View.GONE);
		}
	}

	/**
	 * Setup result for media send
	 * 
	 * @param success
	 */
	private void setMediaSendResultUI(boolean success) {
		if (success) {
			imgBadMedia.setVisibility(View.GONE);
			imgOKMedia.setVisibility(View.VISIBLE);
		} else {
			imgBadMedia.setVisibility(View.VISIBLE);
			imgOKMedia.setVisibility(View.GONE);
		}
	}
	
	
}