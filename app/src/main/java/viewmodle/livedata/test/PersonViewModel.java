package viewmodle.livedata.test;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class PersonViewModel extends ViewModel {


    private MutableLiveData<Person> personLiveData = null;

    public PersonViewModel() {
        personLiveData = new MutableLiveData<Person>();
    }

    public LiveData<Person> getPersonLiveData() {
        return personLiveData;
    }
}
