package com.tmp.task_mgmt_service.utills;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class CommonServices {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonServices.class);
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * The <code>generateGenericSuccessResponse</code> is used to send the generic
	 * success response in case of 200 OK.
	 *
	 * @param object
	 * @return
	 */
	public GenericResponse generateGenericSuccessResponse(final Object object) {

		LOGGER.info(ApplicationConstants.CALLED_LABEL);
		return new GenericResponse(ApplicationResponseConstants.SUCCESS_RESPONSE,
				ApplicationResponseConstants.SUCCESS_RESPONSE_MESSAGE, object);

	}
	
	/**
	 * The <code>generateBadResponseWithMessageKey</code> is used to generate bad
	 * response with messageKey
	 *
	 * @param messageKey
	 * @return
	 */
	public GenericResponse generateBadResponseWithMessage(final String message) {

		LOGGER.info(ApplicationConstants.CALLED_LABEL);
		return new GenericResponse(ApplicationResponseConstants.INVALID_REQUEST, message);
	}
	
	public String getMessageByCode(final String string) {

		LOGGER.info(ApplicationConstants.CALLED_LABEL);
		return getMessageSource().getMessage(string, null, Locale.US);
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

}
