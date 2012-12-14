package com.googlecode.wicket.jquery.ui.samples.pages.menu;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.wicket.jquery.ui.widget.menu.Menu;
import com.googlecode.wicket.jquery.ui.widget.menu.MenuItem;

public class DefaultMenuPage extends AbstractMenuPage
{
	private static final long serialVersionUID = 1L;

	public DefaultMenuPage()
	{
		List<MenuItem> items = new ArrayList<MenuItem>();
		items.add(new MenuItem("Save"));
		items.add(new MenuItem("Zoom in"));
		items.add(new MenuItem("Zoom out"));

		this.add(new Menu("menu", items));
	}
}
