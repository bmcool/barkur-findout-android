package barkur.indout.android.model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ModelCategory implements Serializable {
	public int id;
	public String title;
	public String icon;
	public ArrayList<Integer> tags;
}
