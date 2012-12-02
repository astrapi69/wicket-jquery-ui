package com.googlecode.wicket.jquery.ui.samples.pages.spinner;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.form.button.Button;
import com.googlecode.wicket.jquery.ui.form.spinner.Spinner;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;

public class OptionSpinnerPage extends AbstractSpinnerPage
{
	private static final long serialVersionUID = 1L;
	private FeedbackPanel feedback;

	public OptionSpinnerPage()
	{
		this.init();
	}

	private void init()
	{
		final Form<Float> form = new Form<Float>("form", new Model<Float>(10f));
		this.add(form);

		// FeedbackPanel //
		this.feedback = new JQueryFeedbackPanel("feedback");
		form.add(this.feedback.setOutputMarkupId(true));

		// Spinner //
		Spinner<Float> spinner = new Spinner<Float>("spinner", form.getModel(), Float.class);
		spinner.setCulture("fr").setNumberFormat("C");
		spinner.setMin(1).setMax(100);
		spinner.setStep(5).setPage(4);
		spinner.setRequired(true);

		form.add(spinner);

		// Buttons //
		form.add(new Button("submit") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				OptionSpinnerPage.this.info(this, form);
			}
		});

		form.add(new AjaxButton("button") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(OptionSpinnerPage.this.feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> unused)
			{
				OptionSpinnerPage.this.info(this, form);
				target.add(form);
			}
		});
	}

	private void info(Component component, Form<Float> form)
	{
		this.info(component.getMarkupId() + " has been clicked");
		this.info("The model object is: " + form.getModelObject());
	}
}
