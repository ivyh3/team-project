package interface_adapter.view_model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Upload Materials view.
 * Stores the state and data that the upload materials view needs to display.
 */
public class UploadMaterialsViewModel {
	private final PropertyChangeSupport support;
	
	private List<String> uploadedMaterials;
	private boolean uploadInProgress;
	private String statusMessage;
	private String errorMessage;
	private int uploadProgress;
	
	public UploadMaterialsViewModel() {
		this.support = new PropertyChangeSupport(this);
		this.uploadedMaterials = new ArrayList<>();
		this.uploadInProgress = false;
		this.statusMessage = "";
		this.errorMessage = "";
		this.uploadProgress = 0;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	public List<String> getUploadedMaterials() {
		return new ArrayList<>(uploadedMaterials);
	}
	
	public void setUploadedMaterials(List<String> uploadedMaterials) {
		List<String> oldValue = this.uploadedMaterials;
		this.uploadedMaterials = new ArrayList<>(uploadedMaterials);
		support.firePropertyChange("uploadedMaterials", oldValue, this.uploadedMaterials);
	}
	
	public void addMaterial(String materialName) {
		List<String> oldValue = new ArrayList<>(this.uploadedMaterials);
		this.uploadedMaterials.add(materialName);
		support.firePropertyChange("uploadedMaterials", oldValue, this.uploadedMaterials);
	}
	
	public void removeMaterial(String materialName) {
		List<String> oldValue = new ArrayList<>(this.uploadedMaterials);
		this.uploadedMaterials.remove(materialName);
		support.firePropertyChange("uploadedMaterials", oldValue, this.uploadedMaterials);
	}
	
	public boolean isUploadInProgress() {
		return uploadInProgress;
	}
	
	public void setUploadInProgress(boolean uploadInProgress) {
		boolean oldValue = this.uploadInProgress;
		this.uploadInProgress = uploadInProgress;
		support.firePropertyChange("uploadInProgress", oldValue, uploadInProgress);
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public void setStatusMessage(String statusMessage) {
		String oldValue = this.statusMessage;
		this.statusMessage = statusMessage;
		support.firePropertyChange("statusMessage", oldValue, statusMessage);
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		String oldValue = this.errorMessage;
		this.errorMessage = errorMessage;
		support.firePropertyChange("errorMessage", oldValue, errorMessage);
	}
	
	public int getUploadProgress() {
		return uploadProgress;
	}
	
	public void setUploadProgress(int uploadProgress) {
		int oldValue = this.uploadProgress;
		this.uploadProgress = uploadProgress;
		support.firePropertyChange("uploadProgress", oldValue, uploadProgress);
	}
}

