'use strict';

goog.provide('Blockly.Blocks.xapiverbs');

goog.require('Blockly.Blocks.Utilities');


Blockly.Blocks['accessed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Accessed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('accessed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has accessed the object. For instance, a person accessing a room, or accessing a file.');
		  },
		  typeblock: [{ translatedName: 'accessedBlock' }]
};


Blockly.Blocks['accepted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Accepted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('accepted');
		    this.setOutput(true);
		    this.setTooltip('Indicates that that the actor has accepted the object. For instance, a person accepting an award, or accepting an assignment.');
		  },
		  typeblock: [{ translatedName: 'acceptedBlock' }]
};


Blockly.Blocks['added'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Added xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('added');
		    this.setOutput(true);
		    this.setTooltip('Added one or more items to a collection.');
		  },
		  typeblock: [{ translatedName: 'addedBlock' }]
};


Blockly.Blocks['adjourned'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Adjourned xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('adjourned');
		    this.setOutput(true);
		    this.setTooltip('Indicates the actor temporarily ended an event (e.g. a meeting). It is expected (but not required) that the event will be resumed at a future point in time. The actor of the statement should be somebody who has authority to adjourn the event, for example the event organizer.');
		  },
		  typeblock: [{ translatedName: 'adjournedBlock' }]
};


Blockly.Blocks['applauded'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Applauded xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('applauded');
		    this.setOutput(true);
		    this.setTooltip('indicates that the actor approves of the content or message. Analogous to praising.');
		  },
		  typeblock: [{ translatedName: 'applaudedBlock' }]
};


Blockly.Blocks['arranged'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Arranged xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('arranged');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor arranged the object within a collection or set of elements. The extension http://id.tincanapi.com/extension/position should be used to indicate the new position.');
		  },
		  typeblock: [{ translatedName: 'arrangedBlock' }]
};


Blockly.Blocks['asked'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Asked xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('asked');
		    this.setOutput(true);
		    this.setTooltip('Used to make an inquiry of an actor with the expectation of a response. May be used to ask a question, typically the system would be the primary actor, with the learner being the recipient of the question. The question could also be asked into a vacuum, with the eventual response (statement with verb responded) providing the actual context of the recipient. For example \"System asked Math quiz question 1 with result \"What is 2+2\"\" followed by "Andy responded to quiz question 1 with result \"response=\"4\"\" would alleviate the need to identify the second actor.');
		  },
		  typeblock: [{ translatedName: 'askedBlock' }]
};


Blockly.Blocks['attempted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Attempted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('attempted');
		    this.setOutput(true);
		    this.setTooltip('Used at the initiation of many \"experienced\" activities to mark the entry. Attempts without further verbs could be incomplete in some cases.');
		  },
		  typeblock: [{ translatedName: 'attemptedBlock' }]
};


Blockly.Blocks['attended'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Attended xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('attended');
		    this.setOutput(true);
		    this.setTooltip('The process of associating the location of an actor to some place (physical or virtual).');
		  },
		  typeblock: [{ translatedName: 'attendedBlock' }]
};


Blockly.Blocks['acknowledged'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Acknowledged xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('acknowledged');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has acknowledged the object. This effectively signals that the actor is aware of the objects existence.');
		  },
		  typeblock: [{ translatedName: 'acknowledgedBlock' }]
};


Blockly.Blocks['added'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Added xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('added');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has added the object to the target. For instance, adding a photo to an album.');
		  },
		  typeblock: [{ translatedName: 'addedBlock' }]
};


Blockly.Blocks['agreed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Agreed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('agreed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor agrees with the object. For example, a person agreeing with an argument, or expressing agreement with a particular issue.');
		  },
		  typeblock: [{ translatedName: 'agreedBlock' }]
};


Blockly.Blocks['answered'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Answered xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('answered');
		    this.setOutput(true);
		    this.setTooltip('Indicates the actor responded to a Question.');
		  },
		  typeblock: [{ translatedName: 'answeredBlock' }]
};


Blockly.Blocks['annotated'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Annotated xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('annotated');
		    this.setOutput(true);
		    this.setTooltip('Indicates a new annotation has been added to a document. This verb may be used with PDFs, images, assignment submissions or any other type of document which may be annotated.');
		  },
		  typeblock: [{ translatedName: 'annotatedBlock' }]
};


Blockly.Blocks['appended'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Appended xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('appended');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has appended the object to the target. For instance, a person appending a new record to a database.');
		  },
		  typeblock: [{ translatedName: 'appendedBlock' }]
};


Blockly.Blocks['approved'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Approved xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('approved');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has approved the object. For instance, a manager might approve a travel request.');
		  },
		  typeblock: [{ translatedName: 'approvedBlock' }]
};


Blockly.Blocks['archived'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Archived xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('archived');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has archived the object.');
		  },
		  typeblock: [{ translatedName: 'archivedBlock' }]
};


Blockly.Blocks['assigned'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Assigned xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('assigned');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has assigned the object to the target.');
		  },
		  typeblock: [{ translatedName: 'assignedBlock' }]
};


Blockly.Blocks['attached'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Attached xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('attached');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has attached the object to the target. For instance, a person attaching a file to a wiki page or an email.');
		  },
		  typeblock: [{ translatedName: 'attachedBlock' }]
};


Blockly.Blocks['attended'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Attended xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('attended');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has attended the object. For instance, a person attending a meeting.');
		  },
		  typeblock: [{ translatedName: 'attendedBlock' }]
};


Blockly.Blocks['authored'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Authored xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('authored');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has authored the object. Note that this is a more specific form of the verb \"create\".');
		  },
		  typeblock: [{ translatedName: 'authoredBlock' }]
};


Blockly.Blocks['authorized'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Authorized xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('authorized');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has authorized the object. If a target is specified, it means that the authorization is specifically in regards to the target. For instance, a service can authorize a person to access a given application; in which case the actor is the service, the object is the person, and the target is the application. In contrast, a person can authorize a request; in which case the actor is the person and the object is the request and there might be no explicit target.');
		  },
		  typeblock: [{ translatedName: 'authorizedBlock' }]
};


Blockly.Blocks['bookmarked'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Bookmarked xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('bookmarked');
		    this.setOutput(true);
		    this.setTooltip('Indicates the user determined the content was important enough to keep a reference to it for later. A different verb should be used for tracking the location in a set of text that a reader has reached, as in a physical bookmark.');
		  },
		  typeblock: [{ translatedName: 'bookmarkedBlock' }]
};


Blockly.Blocks['borrowed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Borrowed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('borrowed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has borrowed the object. If a target is specified, it identifies the entity from which the object was borrowed. For instance, if a person borrows a book from a library, the person is the actor, the book is the object and the library is the target.');
		  },
		  typeblock: [{ translatedName: 'borrowedBlock' }]
};


Blockly.Blocks['built'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Built xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('built');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has built the object. For example, if a person builds a model or compiles code.');
		  },
		  typeblock: [{ translatedName: 'builtBlock' }]
};


Blockly.Blocks['called'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Called xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('called');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor placed a phone call to the object.');
		  },
		  typeblock: [{ translatedName: 'calledBlock' }]
};


Blockly.Blocks['canceled'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Canceled xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('canceled');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has canceled the object. For instance, canceling a calendar event.');
		  },
		  typeblock: [{ translatedName: 'canceledBlock' }]
};


Blockly.Blocks['cancelledplannedlearning'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Cancelled planned learning xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('cancelled planned learning');
		    this.setOutput(true);
		    this.setTooltip('This verb is a weaker version of "http://adlnet.gov/expapi/verbs/voided" and is used to let a Learning Planning System know that this particular plan has been superseded and is no longer relevant. It implies a change of plans, whereas voiding the statement would indicate that the plan had never actually been made in the first place. The object of this verb should always be a statement reference pointed to the id of statement using either the "http://www.tincanapi.co.uk/verbs/planned_learning" or "http://www.tincanapi.co.uk/verbs/enrolled_onto_learning_plan" verbs. Where a whole learning plan is cancelled, every item in that plan is considered to be cancelled. "http://adlnet.gov/expapi/verbs/voided" would suggest that the plan was never made in the first place.');
		  },
		  typeblock: [{ translatedName: 'cancelledplannedlearningBlock' }]
};


Blockly.Blocks['createdopportunity'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Created opportunity xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('created opportunity');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has created a new opportunity, such as one might do in a CRM tool.');
		  },
		  typeblock: [{ translatedName: 'createdopportunityBlock' }]
};


Blockly.Blocks['checkedin'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Checked in xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('checked in');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has checked-in to the object. For instance, a person checking-in to a place.');
		  },
		  typeblock: [{ translatedName: 'checkedinBlock' }]
};


Blockly.Blocks['closed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Closed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('closed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has closed the object. For instance, the object could represent a ticket being tracked in an issue management system.');
		  },
		  typeblock: [{ translatedName: 'closedBlock' }]
};


Blockly.Blocks['closedsale'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Closed sale xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('closed sale');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has closed a sale.');
		  },
		  typeblock: [{ translatedName: 'closedsaleBlock' }]
};


Blockly.Blocks['commented'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Commented xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('commented');
		    this.setOutput(true);
		    this.setTooltip('Offered an opinion or written experience of the activity. Can be used with the learner as the actor or a system as an actor. Comments can be sent from either party with the idea that the other will read and react to the content.');
		  },
		  typeblock: [{ translatedName: 'commentedBlock' }]
};


Blockly.Blocks['completed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Completed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('completed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has completed the object.');
		  },
		  typeblock: [{ translatedName: 'completedBlock' }]
};


Blockly.Blocks['completed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Completed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('completed');
		    this.setOutput(true);
		    this.setTooltip('To experience the activity in its entirety. Used to affirm the completion of content. This can be simply experiencing all the content, be tied to objectives or interactions, or determined in any other way. Any content that has been initialized, but not yet completed, should be considered incomplete. There is no verb to \"incomplete\" an activity, one would void the statement which completes the activity.');
		  },
		  typeblock: [{ translatedName: 'completedBlock' }]
};


Blockly.Blocks['confirmed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Confirmed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('confirmed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has confirmed or agrees with the object. For instance, a software developer might confirm an issue reported against a product.');
		  },
		  typeblock: [{ translatedName: 'confirmedBlock' }]
};


Blockly.Blocks['consumed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Consumed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('consumed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has consumed the object. The specific meaning is dependent largely on the objects type. For instance, an actor may \"consume\" an audio object, indicating that the actor has listened to it; or an actor may \"consume\" a book, indicating that the book has been read. As such, the \"consume\" verb is a more generic form of other more specific verbs such as \"read\" and \"play\".');
		  },
		  typeblock: [{ translatedName: 'consumedBlock' }]
};


Blockly.Blocks['created'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Created xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('created');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has created the object.');
		  },
		  typeblock: [{ translatedName: 'createdBlock' }]
};


Blockly.Blocks['defined'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Defined xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('defined');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has defined the object. Note that this is a more specific form of the verb \"create\". For instance, a learner defining a goal.');
		  },
		  typeblock: [{ translatedName: 'definedBlock' }]
};


Blockly.Blocks['deleted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Deleted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('deleted');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has deleted the object. This implies, but does not require, the permanent destruction of the object.');
		  },
		  typeblock: [{ translatedName: 'deletedBlock' }]
};


Blockly.Blocks['delivered'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Delivered xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('delivered');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has delivered the object. For example, delivering a package.');
		  },
		  typeblock: [{ translatedName: 'deliveredBlock' }]
};


Blockly.Blocks['denied'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Denied xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('denied');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has denied the object. For example, a manager may deny a travel request.');
		  },
		  typeblock: [{ translatedName: 'deniedBlock' }]
};


Blockly.Blocks['discarded'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Discarded xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('discarded');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor discarded a previous selection. This verb works with the verb \"selected\".');
		  },
		  typeblock: [{ translatedName: 'discardedBlock' }]
};


Blockly.Blocks['disabled'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Disabled xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('disabled');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor turned off a particular part or feature of the system.');
		  },
		  typeblock: [{ translatedName: 'disabledBlock' }]
};


Blockly.Blocks['disagreed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Disagreed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('disagreed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor disagrees with the object.');
		  },
		  typeblock: [{ translatedName: 'disagreedBlock' }]
};


Blockly.Blocks['disliked'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Disliked xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('disliked');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor dislikes the object. Note that the \"dislike\" verb is distinct from the \"unlike\" verb which assumes that the object had been previously \"liked\".');
		  },
		  typeblock: [{ translatedName: 'dislikedBlock' }]
};


Blockly.Blocks['downloaded'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Downloaded xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('downloaded');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor downloaded (rather than accessed or opened) a file or document.');
		  },
		  typeblock: [{ translatedName: 'downloadedBlock' }]
};


Blockly.Blocks['downvoted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Down voted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('down voted');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has voted down for a specific object. This is analogous to giving a thumbs down.');
		  },
		  typeblock: [{ translatedName: 'downvotedBlock' }]
};


Blockly.Blocks['drew'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Drew xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('drew');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has created a picture of something using a physical drawing utensil or a digital input device.');
		  },
		  typeblock: [{ translatedName: 'drewBlock' }]
};


Blockly.Blocks['earned'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Earned xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('earned');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has earned or has been awarded the object.');
		  },
		  typeblock: [{ translatedName: 'earnedBlock' }]
};


Blockly.Blocks['earnedanopenbadge'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Earned an Open Badge xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('earned an open badge');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has been recognized by an Open Badge issuer for an achievement. The actor may claim the badge referenced as the object and use it as a verifiable credential, wherever Open Badges are accepted.');
		  },
		  typeblock: [{ translatedName: 'earnedanopenbadgeBlock' }]
};


Blockly.Blocks['edited'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Edited xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('edited');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor edited an object, for example a user editing their account profile.');
		  },
		  typeblock: [{ translatedName: 'editedBlock' }]
};


Blockly.Blocks['enabled'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Enabled xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('enabled');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor turned on a particular part or feature of the system. It works with the verb disabled.');
		  },
		  typeblock: [{ translatedName: 'enabledBlock' }]
};


Blockly.Blocks['enrolledontolearningplan'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Enrolled onto learning plan xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('enrolled onto learning plan');
		    this.setOutput(true);
		    this.setTooltip('This verb is used to add additional learners to an existing learning plan, or a new plan if one does not exist. The actor of this statement is the person who is being enrolled onto the plan. Where the enrolment is being assigned by a 3rd party (or the plan was created by a 3rd party), the context instructor property may be used. The object of statements using this verb will always be an activity representing the learning plan. See http://tincanapi.co.uk/pages/The_Learning_Plan_Framework.html for more details.');
		  },
		  typeblock: [{ translatedName: 'enrolledontolearningplanBlock' }]
};


Blockly.Blocks['enteredframe'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Entered frame xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('entered frame');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has entered the frame of a camera or viewing device.');
		  },
		  typeblock: [{ translatedName: 'enteredframeBlock' }]
};


Blockly.Blocks['estimatedtheduration'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Estimated the duration xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('estimated the duration');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has estimated the duration of the object. For instance, a learner estimating the duration of a task.');
		  },
		  typeblock: [{ translatedName: 'estimatedthedurationBlock' }]
};


Blockly.Blocks['evaluated'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Evaluated xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('evaluated');
		    this.setOutput(true);
		    this.setTooltip('Verb used for evaluating a previous learning experience. The object of the statement should normally be a StatementRef pointing to an existing statement about the experience being evaluated. The actual evaluation should be provided in the result as either a score, response or both. See http://tincanapi.co.uk/pages/Tin_Can_Learning_Evaluator.html#Evaluating_and_Reflecting for further details and examples.');
		  },
		  typeblock: [{ translatedName: 'evaluatedBlock' }]
};


Blockly.Blocks['exited'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Exited xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('exited');
		    this.setOutput(true);
		    this.setTooltip('Used to leave an activity attempt with no intention of returning with the learner progress intact. The expectation is learner progress will be cleared. Should appear immediately before a statement with terminated. A statement with EITHER exited OR suspended should be used before one with terminated. Lack of the two implies the same as exited.');
		  },
		  typeblock: [{ translatedName: 'exitedBlock' }]
};


Blockly.Blocks['exitedframe'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Exited frame xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('exited frame');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has exited the frame of a camera or viewing device.');
		  },
		  typeblock: [{ translatedName: 'exitedframeBlock' }]
};


Blockly.Blocks['expected'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Expected xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('expected');
		    this.setOutput(true);
		    this.setTooltip('Indicates that that the actor has accepted the object. For instance, a person accepting an award, or accepting an assignment.');
		  },
		  typeblock: [{ translatedName: 'expectedBlock' }]
};


Blockly.Blocks['experienced'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Experienced xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('experienced');
		    this.setOutput(true);
		    this.setTooltip('A catch-all verb to say that someone viewed, listened to, read, etc. some form of content. There is no assumption of completion or success.');
		  },
		  typeblock: [{ translatedName: 'experiencedBlock' }]
};


Blockly.Blocks['experienced'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Experienced xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('experienced');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has experienced the object in some manner. Note that, depending on the specific object types used for both the actor and object, the meaning of this verb can overlap that of the \"consume\" and \"play\" verbs. For instance, a person might \"experience\" a movie; or \"play\" the movie; or \"consume\" the movie. The \"experience\" verb can be considered a more generic form of other more specific verbs as \"consume\", \"play\", \"watch\", \"listen\", and \"read\"');
		  },
		  typeblock: [{ translatedName: 'experiencedBlock' }]
};


Blockly.Blocks['expired'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Expired xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('expired');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the object (a competency or certification) has expired for the actor.');
		  },
		  typeblock: [{ translatedName: 'expiredBlock' }]
};


Blockly.Blocks['failed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Failed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('failed');
		    this.setOutput(true);
		    this.setTooltip('Learner did not perform the activity to a level of pre-determined satisfaction. Used to affirm the lack of success a learner experienced within the learning content in relation to a threshold. If the user performed below the minimum to the level of this threshold, the content is \"failed\". The opposite of \"passed\".');
		  },
		  typeblock: [{ translatedName: 'failedBlock' }]
};


Blockly.Blocks['favorited'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Favorited xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('favorited');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor marked the object as an item of special interest.');
		  },
		  typeblock: [{ translatedName: 'favoritedBlock' }]
};


Blockly.Blocks['flaggedasinappropriate'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Flagged as inappropriate xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('flagged as inappropriate');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has flagged the object as being inappropriate for some reason. When using this verb, the context property, as specified within Section 4.1 can be used to provide additional detail about why the object has been flagged.');
		  },
		  typeblock: [{ translatedName: 'flaggedasinappropriateBlock' }]
};


Blockly.Blocks['focused'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Focused xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('focused');
		    this.setOutput(true);
		    this.setTooltip('Indicates that a user has focused on a target object. This is the opposite of \"unfocused\". For example, it indicates that the user has clicked to focus or regain focus on the application, content or activity.');
		  },
		  typeblock: [{ translatedName: 'focusedBlock' }]
};


Blockly.Blocks['followed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Followed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('followed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor began following the activity of the object. In most cases, the objectType will be a \"person\", but it can potentially be of any type that can sensibly generate activity. Processors MAY ignore (silently drop) successive identical \"follow\" activities.');
		  },
		  typeblock: [{ translatedName: 'followedBlock' }]
};


Blockly.Blocks['found'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Found xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('found');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has found the object.');
		  },
		  typeblock: [{ translatedName: 'foundBlock' }]
};


Blockly.Blocks['gave'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Gave xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('gave');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor is giving an object to the target. Examples include one person giving a badge object to another person. The object identifies the object being given. The target identifies the receiver.');
		  },
		  typeblock: [{ translatedName: 'gaveBlock' }]
};


Blockly.Blocks['hired'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Hired xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('hired');
		    this.setOutput(true);
		    this.setTooltip('An offer of employment that has been made by an agent and accepted by another.');
		  },
		  typeblock: [{ translatedName: 'hiredBlock' }]
};


Blockly.Blocks['hosted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Hosted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('hosted');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor is hosting the object. As in hosting an event, or hosting a service.');
		  },
		  typeblock: [{ translatedName: 'hostedBlock' }]
};


Blockly.Blocks['ignored'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Ignored xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('ignored');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has ignored the object. For instance, this verb may be used when an actor has ignored a friend request, in which case the object may be the request-friend activity.');
		  },
		  typeblock: [{ translatedName: 'ignoredBlock' }]
};


Blockly.Blocks['initialized'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Initialized xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('initialized');
		    this.setOutput(true);
		    this.setTooltip('Begins the formal tracking of learning content, any statements time stamped before a statement with an initialized verb are not formally tracked.');
		  },
		  typeblock: [{ translatedName: 'initializedBlock' }]
};


Blockly.Blocks['inserted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Inserted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('inserted');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has inserted the object into the target.');
		  },
		  typeblock: [{ translatedName: 'insertedBlock' }]
};


Blockly.Blocks['installed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Installed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('installed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has installed the object, as in installing an application.');
		  },
		  typeblock: [{ translatedName: 'installedBlock' }]
};


Blockly.Blocks['interacted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Interacted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('interacted');
		    this.setOutput(true);
		    this.setTooltip('A catch-all verb used to assert an actors manipulation of an object, physical or digital, in some context.');
		  },
		  typeblock: [{ translatedName: 'interactedBlock' }]
};


Blockly.Blocks['interacted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Interacted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('interacted');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has interacted with the object. For instance, when one person interacts with another.');
		  },
		  typeblock: [{ translatedName: 'interactedBlock' }]
};


Blockly.Blocks['interviewed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Interviewed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('interviewed');
		    this.setOutput(true);
		    this.setTooltip('For use when one agent or group interviews another agent or group. It could be used for the purposes of hiring, creating news articles, shows, research, etc.');
		  },
		  typeblock: [{ translatedName: 'interviewedBlock' }]
};


Blockly.Blocks['invited'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Invited xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('invited');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has invited the object, typically a person object, to join or participate in the object described by the target. The target could, for instance, be an event, group or a service.');
		  },
		  typeblock: [{ translatedName: 'invitedBlock' }]
};


Blockly.Blocks['imported'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Imported xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('imported');
		    this.setOutput(true);
		    this.setTooltip('The act of moving an object into another location or system.');
		  },
		  typeblock: [{ translatedName: 'importedBlock' }]
};


Blockly.Blocks['joined'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Joined xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('joined');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has become a member of the object. This specification only defines the meaning of this verb when the object of the Activity has an objectType of group, though implementors need to be prepared to handle other types of objects.');
		  },
		  typeblock: [{ translatedName: 'joinedBlock' }]
};


Blockly.Blocks['laughed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Laughed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('laughed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor found the content funny and enjoyable. May be used with an "Ending Point" extension (see http://id.tincanapi.com/extension/ending-point) value capturing the point in time within the Activity.');
		  },
		  typeblock: [{ translatedName: 'laughedBlock' }]
};


Blockly.Blocks['launched'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Launched xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('launched');
		    this.setOutput(true);
		    this.setTooltip('Starts the process of launching the next piece of learning content. There is no expectation if this is done by user or system and no expectation that the learning content is a "SCO". It is highly recommended that the display is used to mirror the behavior. If an activity is launched from another, then launched from may be better. If the activity is launched and then the statement is generated, launched or launched into may be more appropriate.');
		  },
		  typeblock: [{ translatedName: 'launchedBlock' }]
};


Blockly.Blocks['left'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Left xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('left');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has left the object. For instance, a Person leaving a Group or checking-out of a Place.');
		  },
		  typeblock: [{ translatedName: 'leftBlock' }]
};


Blockly.Blocks['liked'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Liked xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('liked');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor marked the object as an item of special interest. The \"like\" verb is considered to be an alias of \"favorite\". The two verb are semantically identical.');
		  },
		  typeblock: [{ translatedName: 'likedBlock' }]
};


Blockly.Blocks['listened'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Listened xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('listened');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has listened to the object. This is typically only applicable for objects representing audio content, such as music, an audio-book, or a radio broadcast. The \"listen\" verb is a more specific form of the \"consume\", \"experience\" and \"play\" verbs.');
		  },
		  typeblock: [{ translatedName: 'listenedBlock' }]
};


Blockly.Blocks['login'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Log In xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('log in');
		    this.setOutput(true);
		    this.setTooltip('Logged in to some service.');
		  },
		  typeblock: [{ translatedName: 'loginBlock' }]
};


Blockly.Blocks['logout'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Log Out xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('log out');
		    this.setOutput(true);
		    this.setTooltip('Logged out of some service.');
		  },
		  typeblock: [{ translatedName: 'logoutBlock' }]
};


Blockly.Blocks['lost'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Lost xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('lost');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has lost the object. For instance, if a person loses a game.');
		  },
		  typeblock: [{ translatedName: 'lostBlock' }]
};


Blockly.Blocks['madefriend'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Made friend xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('made friend');
		    this.setOutput(true);
		    this.setTooltip('Indicates the creation of a friendship that is reciprocated by the object. Since this verb implies an activity on the part of its object, processors MUST NOT accept activities with this verb unless they are able to verify through some external means that there is in fact a reciprocated connection. For example, a processor may have received a guarantee from a particular publisher that the publisher will only use this Verb in cases where a reciprocal relationship exists.');
		  },
		  typeblock: [{ translatedName: 'madefriendBlock' }]
};


Blockly.Blocks['mastered'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Mastered xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('mastered');
		    this.setOutput(true);
		    this.setTooltip('Used to describe a level of competence achieved in the activity. The level should be within the range of a defined scale. This is not to be confused with \"progressed\", which shows how much content was experienced, whereas mastery has to do with level of expertise.');
		  },
		  typeblock: [{ translatedName: 'masteredBlock' }]
};


Blockly.Blocks['mentioned'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Mentioned xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('mentioned');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor mentioned the object, for example in a tweet.');
		  },
		  typeblock: [{ translatedName: 'mentionedBlock' }]
};


Blockly.Blocks['mentored'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Mentored xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('mentored');
		    this.setOutput(true);
		    this.setTooltip('Indicates that that the actor has mentored the object. For instance, a manager mentoring an employee, or a teacher mentoring a student.');
		  },
		  typeblock: [{ translatedName: 'mentoredBlock' }]
};


Blockly.Blocks['modifiedannotation'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Modified annotation xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('modified annotation');
		    this.setOutput(true);
		    this.setTooltip('This verb is used on annotations created with the http://risc-inc.com/annotator/verbs/annotated verb. It indicates that an existing annotation has been modified, for example editing the text of a note annotation or adjusting the position of a underline or highlight.');
		  },
		  typeblock: [{ translatedName: 'modifiedannotationBlock' }]
};


Blockly.Blocks['opened'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Opened xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('opened');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has opened the object. For instance, the object could represent a ticket being tracked in an issue management system.');
		  },
		  typeblock: [{ translatedName: 'openedBlock' }]
};


Blockly.Blocks['passed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Passed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('passed');
		    this.setOutput(true);
		    this.setTooltip('Used to affirm the success a learner experienced within the learning content in relation to a threshold. If the user performed at a minimum to the level of this threshold, the content is \"passed\". The opposite of \"failed\".');
		  },
		  typeblock: [{ translatedName: 'passedBlock' }]
};


Blockly.Blocks['paused'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Paused xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('paused');
		    this.setOutput(true);
		    this.setTooltip('To indicate an actor has ceased or suspended an activity temporarily.');
		  },
		  typeblock: [{ translatedName: 'pausedBlock' }]
};


Blockly.Blocks['performed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Performed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('performed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has performed the object offline for a period of time (episode). For instance, a learner performed task X, which is an offline task like reading a book, for 30 minutes. This is used to record the time spent on offline activities.');
		  },
		  typeblock: [{ translatedName: 'performedBlock' }]
};


Blockly.Blocks['personalized'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Personalized xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('personalized');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor personalized the object. The idea is that the actor personalizes an object created by a third party to adapt it for his/her personal use. This can be used for personalizing a strategy, method, a cooking recipe, etc.');
		  },
		  typeblock: [{ translatedName: 'personalizedBlock' }]
};


Blockly.Blocks['previewed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Previewed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('previewed');
		    this.setOutput(true);
		    this.setTooltip('Used to indicate that an actor has taken a first glance at a piece of content that they plan to return to for a more in depth experience later. For instance someone may come across a webpage that they do not have enough time to read at that time, but plan to come back to and read fully.');
		  },
		  typeblock: [{ translatedName: 'previewedBlock' }]
};


Blockly.Blocks['promoted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Promoted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('promoted');
		    this.setOutput(true);
		    this.setTooltip('The act of promoting a content item such that it appears more highly in search results or is promoted to users in some other way.');
		  },
		  typeblock: [{ translatedName: 'promoteddBlock' }]
};


Blockly.Blocks['planned'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Planned xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('planned');
		    this.setOutput(true);
		    this.setTooltip('Used to assert an intention to undertake a learning experience or activity. The object of the statement using this verb should always be a subStatement. See http://tincanapi.co.uk/pages/I_Want_This.html and http://tincanapi.co.uk/pages/The_Learning_Plan_Framework.html#Planned for more details.');
		  },
		  typeblock: [{ translatedName: 'plannedBlock' }]
};


Blockly.Blocks['played'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Played xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('played');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor spent some time enjoying the object. For example, if the object is a video this indicates that the subject watched all or part of the video. The \"play\" verb is a more specific form of the \"consume\" verb.');
		  },
		  typeblock: [{ translatedName: 'playedBlock' }]
};


Blockly.Blocks['preferred'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Preferred xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('preferred');
		    this.setOutput(true);
		    this.setTooltip('The users preference, typically presented to the content or system in the form of a response to a question. A response to a personal question to the learner, typically resulting in a change in content or system behavior. For example, the system could ask a question if the learner preferred a voice over text option. The resulting statement could be Andy preferred on Civil War History with result response = \"no voiceover\". This distinction is made between statements with responded as the content/system is expected to change as a results of the learner response.');
		  },
		  typeblock: [{ translatedName: 'preferredBlock' }]
};


Blockly.Blocks['presented'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Presented xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('presented');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has presented the object. For instance, when a person gives a presentation at a conference.');
		  },
		  typeblock: [{ translatedName: 'presentedBlock' }]
};


Blockly.Blocks['pressed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Pressed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('pressed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has pressed the object. For instance, a person pressing a key of a keyboard.');
		  },
		  typeblock: [{ translatedName: 'pressedBlock' }]
};


Blockly.Blocks['progressed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Progressed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('progressed');
		    this.setOutput(true);
		    this.setTooltip('A value, typically within a scale of progression, to how much of an activity has been accomplished. This is not to be confused with \"mastered\", as the level of success or competency a user gained is not guaranteed by progress.');
		  },
		  typeblock: [{ translatedName: 'progressedBlock' }]
};


Blockly.Blocks['purchased'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Purchased xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('purchased');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has purchased the object. If a target is specified, in indicates the entity from which the object was purchased.');
		  },
		  typeblock: [{ translatedName: 'purchasedBlock' }]
};


Blockly.Blocks['qualified'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Qualified xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('qualified');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has qualified for the object. If a target is specified, it indicates the context within which the qualification applies.');
		  },
		  typeblock: [{ translatedName: 'qualifiedBlock' }]
};


Blockly.Blocks['ran'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Ran xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('ran');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor ran a distance indicated by the activity.');
		  },
		  typeblock: [{ translatedName: 'ranBlock' }]
};


Blockly.Blocks['rated'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Rated xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('rated');
		    this.setOutput(true);
		    this.setTooltip('Action of giving a rating to an object. Should only be used when the action is the rating itself, as opposed to another action such as "reading" where a rating can be applied to the object as part of that action. In general the rating should be included in the Result with a Score object.');
		  },
		  typeblock: [{ translatedName: 'ratedBlock' }]
};


Blockly.Blocks['read'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Read xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('read');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor read the object. This is typically only applicable for objects representing printed or written content, such as a book, a message or a comment. The \"read\" verb is a more specific form of the \"consume\", \"experience\" and \"play\" verbs.');
		  },
		  typeblock: [{ translatedName: 'readBlock' }]
};


Blockly.Blocks['received'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Received xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('received');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor is receiving an object. Examples include a person receiving a badge object. The object identifies the object being received.');
		  },
		  typeblock: [{ translatedName: 'receivedBlock' }]
};


Blockly.Blocks['registered'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Registered xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('registered');
		    this.setOutput(true);
		    this.setTooltip('Indicates the actor registered for a learning activity.');
		  },
		  typeblock: [{ translatedName: 'registeredBlock' }]
};


Blockly.Blocks['rejected'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Rejected xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('rejected');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has rejected the object.');
		  },
		  typeblock: [{ translatedName: 'rejectedBlock' }]
};


Blockly.Blocks['released'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Released xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('released');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has released the object. For instance, a person releasing a key of a keyboard.');
		  },
		  typeblock: [{ translatedName: 'releasedBlock' }]
};


Blockly.Blocks['removed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Removed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('removed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has removed the object from the target.');
		  },
		  typeblock: [{ translatedName: 'removedBlock' }]
};


Blockly.Blocks['removed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Removed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('removed');
		    this.setOutput(true);
		    this.setTooltip('Removed one or more items from a collection.');
		  },
		  typeblock: [{ translatedName: 'removedBlock' }]
};


Blockly.Blocks['removedfriend'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Removed friend xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('removed friend');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has removed the object from the collection of friends.');
		  },
		  typeblock: [{ translatedName: 'removedfriendBlock' }]
};


Blockly.Blocks['replaced'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Replaced xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('replaced');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has replaced the target with the object.');
		  },
		  typeblock: [{ translatedName: 'replacedBlock' }]
};


Blockly.Blocks['replied'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Replied xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('replied');
		    this.setOutput(true);
		    this.setTooltip('The actor posted a reply to a forum, comment thread or discussion.');
		  },
		  typeblock: [{ translatedName: 'repliedBlock' }]
};


Blockly.Blocks['repliedtotweet'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Replied to tweet xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('replied to tweet');
		    this.setOutput(true);
		    this.setTooltip('This is an extension of the tweeted verb for the specific case of a tweet replying to another. This can be used to track group discussions experience. As with Retweeted we expect to find the original tweet id in the context as well as the persons handle to which the reply is addressed using the tweet extension URI http://id.tincanapi.com/extension/tweet');
		  },
		  typeblock: [{ translatedName: 'repliedtotweetBlock' }]
};


Blockly.Blocks['responded'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Responded xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('responded');
		    this.setOutput(true);
		    this.setTooltip('Used to respond to a question. It could be either the actual answer to a question asked of the actor OR the correctness of an answer to a question asked of the actor. Must follow a statement with asked or another statement with a responded (the top statement with responded) must follow the \"asking\" statement. The response to the question can be the actual text (usually) response or the correctness of that response. For example, Andy responded to quiz question 1 with result \"response=4\" and Andy responded to quiz question 1 with result success=true\". Typically both types of responded statements would follow a single question/interacton.');
		  },
		  typeblock: [{ translatedName: 'respondedBlock' }]
};


Blockly.Blocks['requestedattention'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Requested attention xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('requested attention');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor is requesting the attention of an instructor, presenter or moderator.');
		  },
		  typeblock: [{ translatedName: 'requestedattentionBlock' }]
};


Blockly.Blocks['requested'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Requested xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('requested');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has requested the object. If a target is specified, it indicates the entity from which the object is being requested.');
		  },
		  typeblock: [{ translatedName: 'requestedBlock' }]
};


Blockly.Blocks['requestedfriend'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Requested friend xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('requested friend');
		    this.setOutput(true);
		    this.setTooltip('Indicates the creation of a friendship that has not yet been reciprocated by the object.');
		  },
		  typeblock: [{ translatedName: 'requestedfriendBlock' }]
};


Blockly.Blocks['resolved'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Resolved xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('resolved');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has resolved the object. For instance, the object could represent a ticket being tracked in an issue management system.');
		  },
		  typeblock: [{ translatedName: 'resolvedBlock' }]
};


Blockly.Blocks['resumed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Resumed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('resumed');
		    this.setOutput(true);
		    this.setTooltip('Used to resume suspended attempts on an activity. Should immediately follow a statement with initialized if the attempt is indeed to be resumed. The absence of a resumed statement implies a fresh attempt on the activity. Can only be used on an activity that used a suspended statement.');
		  },
		  typeblock: [{ translatedName: 'resumedBlock' }]
};


Blockly.Blocks['retracted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Retracted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('retracted');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has retracted the object. For instance, if an actor wishes to retract a previously published activity, the object would be the previously published activity that is being retracted.');
		  },
		  typeblock: [{ translatedName: 'retractedBlock' }]
};


Blockly.Blocks['returned'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Returned xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('returned');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has returned the object. If a target is specified, it indicates the entity to which the object was returned.');
		  },
		  typeblock: [{ translatedName: 'returnedBlock' }]
};


Blockly.Blocks['retweeted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Retweeted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('retweeted');
		    this.setOutput(true);
		    this.setTooltip('Used when an agent repeats a tweet written by another user. Usage in a statement is similar to tweeted but we expect to find the URI to the original tweet in the context of the statement, as well as the username of the original author as a context extension. The extension URI used for this should be http://id.tincanapi.com/extension/tweet');
		  },
		  typeblock: [{ translatedName: 'retweetedBlock' }]
};


Blockly.Blocks['reviewed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Reviewed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('reviewed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has reviewed the object. For instance, a person reviewing an employee or a person reviewing an owners manual.');
		  },
		  typeblock: [{ translatedName: 'reviewedBlock' }]
};


Blockly.Blocks['reviewed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Reviewed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('reviewed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor returned to and reviewed the activity.');
		  },
		  typeblock: [{ translatedName: 'reviewedBlock' }]
};


Blockly.Blocks['satisfied'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Satisfied xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('satisfied');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has satisfied the object. If a target is specified, it indicate the context within which the object was satisfied. For instance, if a person satisfies the requirements for a particular challenge, the person is the actor; the requirement is the object; and the challenge is the target.');
		  },
		  typeblock: [{ translatedName: 'satisfiedBlock' }]
};


Blockly.Blocks['saved'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Saved xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('saved');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has called out the object as being of interest primarily to him- or herself. Though this action MAY be shared publicly, the implication is that the object has been saved primarily for the actors own benefit rather than to show it to others as would be indicated by the \"share\" verb.');
		  },
		  typeblock: [{ translatedName: 'savedBlock' }]
};


Blockly.Blocks['scheduled'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Scheduled xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('scheduled');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has scheduled the object. For instance, scheduling a meeting.');
		  },
		  typeblock: [{ translatedName: 'scheduledBlock' }]
};


Blockly.Blocks['scored'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Scored xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('scored');
		    this.setOutput(true);
		    this.setTooltip('A measure related to the learners performance, typically between either 0 and 1 or 0 and 100, which corresponds to a learners performance on an activity. It is expected the context property provides guidance to the allowed values of the result field.');
		  },
		  typeblock: [{ translatedName: 'scoredBlock' }]
};


Blockly.Blocks['searched'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Searched xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('searched');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor is or has searched for the object. If a target is specified, it indicates the context within which the search is or has been conducted.');
		  },
		  typeblock: [{ translatedName: 'searchedBlock' }]
};


Blockly.Blocks['secured'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Secured xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('secured');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor secured the object. The object used with this verb might be a device, piece of software, location, etc.');
		  },
		  typeblock: [{ translatedName: 'securedBlock' }]
};


Blockly.Blocks['selected'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Selected xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('selected');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor selects an object from a collection or set to use it in an activity. The collection would be the context parent element.');
		  },
		  typeblock: [{ translatedName: 'selectedBlock' }]
};


Blockly.Blocks['sent'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Sent xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('sent');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has sent the object. If a target is specified, it indicates the entity to which the object was sent.');
		  },
		  typeblock: [{ translatedName: 'sentBlock' }]
};


Blockly.Blocks['shared'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Shared xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('shared');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has called out the object to readers. In most cases, the actor did not create the object being shared, but is instead drawing attention to it.');
		  },
		  typeblock: [{ translatedName: 'sharedBlock' }]
};


Blockly.Blocks['shared'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Shared xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('shared');
		    this.setOutput(true);
		    this.setTooltip('Generic term indicating the intent to exchange an item of interest or the explicit changing of privacy largely derived from context.');
		  },
		  typeblock: [{ translatedName: 'sharedBlock' }]
};


Blockly.Blocks['skipped'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Skipped xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('skipped');
		    this.setOutput(true);
		    this.setTooltip('To indicate an actor has passed over or omitted an interval, screen, segment, item, or step.');
		  },
		  typeblock: [{ translatedName: 'skippedBlock' }]
};


Blockly.Blocks['sold'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Sold xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('sold');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has sold the object. If a target is specified, it indicates the entity to which the object was sold.');
		  },
		  typeblock: [{ translatedName: 'soldBlock' }]
};


Blockly.Blocks['sponsored'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Sponsored xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('sponsored');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has sponsored the object. If a target is specified, it indicates the context within which the sponsorship is offered. For instance, a company can sponsor an event; or an individual can sponsor a project; etc.');
		  },
		  typeblock: [{ translatedName: 'sponsoredBlock' }]
};


Blockly.Blocks['started'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Started xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('started');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has started the object. For instance, when a person starts a project.');
		  },
		  typeblock: [{ translatedName: 'startedBlock' }]
};


Blockly.Blocks['stoppedfollowing'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Stopped following xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('stopped following');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has stopped following the object.');
		  },
		  typeblock: [{ translatedName: 'stoppedfollowingBlock' }]
};


Blockly.Blocks['submitted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Submitted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('submitted');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has submitted the object. If a target is specified, it indicates the entity to which the object was submitted.');
		  },
		  typeblock: [{ translatedName: 'submittedBlock' }]
};


Blockly.Blocks['suspended'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Suspended xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('suspended');
		    this.setOutput(true);
		    this.setTooltip('Used to suspend an activity with the intention of returning to it later, but not losing progress. Should appear immediately before a statement with terminated. A statement with EITHER exited OR suspended should be used before one with terminated. Lack of the two implies the same as exited. Beginning the suspended activity will always result in a resumed activity.');
		  },
		  typeblock: [{ translatedName: 'suspendedBlock' }]
};


Blockly.Blocks['tagged'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Tagged xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('tagged');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has associated the object with the target. For example, if the actor specifies that a particular user appears in a photo. the object is the user and the target is the photo.');
		  },
		  typeblock: [{ translatedName: 'taggedBlock' }]
};


Blockly.Blocks['talked'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Talked xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('talked');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor talked to another agent or group. The object of statements using this verb should be an agent or group, for example a teacher, an NPC in a simulation, a group of colleagues. This verb is intended to be used where one actor initiates and leads a conversation, rather than an equal discussion between two parties.');
		  },
		  typeblock: [{ translatedName: 'talkedBlock' }]
};


Blockly.Blocks['terminated'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Terminated xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('terminated');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has terminated the object.');
		  },
		  typeblock: [{ translatedName: 'terminatedBlock' }]
};


Blockly.Blocks['terminatedemploymentwith'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Terminated employment with xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('terminated employment with');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actors employment with the organization represented by the object of the statement has been terminated for some reason. Use of this verb does not imply any particular reason for the termination. The actor may have been fired, quit, died etc.');
		  },
		  typeblock: [{ translatedName: 'terminatedemploymentwithBlock' }]
};


Blockly.Blocks['terminated'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Terminated xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('terminated');
		    this.setOutput(true);
		    this.setTooltip('Ends the formal tracking of learning content, any statements time stamped after a statement with a terminated verb are not formally tracked.');
		  },
		  typeblock: [{ translatedName: 'terminatedBlock' }]
};


Blockly.Blocks['tied'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Tied xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('tied');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has neither won or lost the object. This verb is generally only applicable when the object represents some form of competition, such as a game.');
		  },
		  typeblock: [{ translatedName: 'tiedBlock' }]
};


Blockly.Blocks['tweeted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Tweeted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('tweeted');
		    this.setOutput(true);
		    this.setTooltip('Use this verb when an agent tweets on Twitter. It is open for use also for other short messages (microblogging services) based on the URI as the activityId. We expect activityId to be a URI to the tweet.');
		  },
		  typeblock: [{ translatedName: 'tweetedBlock' }]
};


Blockly.Blocks['unfavorited'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Unfavorited xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('unfavorited');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has removed the object from the collection of favorited items.');
		  },
		  typeblock: [{ translatedName: 'unfavoritedBlock' }]
};


Blockly.Blocks['unfocused'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Unfocused xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('unfocused');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the user has lost focus of the target object. For example, this could be used when the user clicks outside a given application, window or activity.');
		  },
		  typeblock: [{ translatedName: 'unfocusedBlock' }]
};


Blockly.Blocks['unliked'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Unliked xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('unliked');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has removed the object from the collection of liked items.');
		  },
		  typeblock: [{ translatedName: 'unlikedBlock' }]
};


Blockly.Blocks['unread'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Unread xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('unread');
		    this.setOutput(true);
		    this.setTooltip('The object was marked as unread.');
		  },
		  typeblock: [{ translatedName: 'unreadBlock' }]
};


Blockly.Blocks['unregistered'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Unregistered xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('unregistered');
		    this.setOutput(true);
		    this.setTooltip('Indicates the actor unregistered for a learning activity. This verb is used in combination with http://adlnet.gov/expapi/verbs/registered for the registering and unregistering of learners.');
		  },
		  typeblock: [{ translatedName: 'unregisteredBlock' }]
};


Blockly.Blocks['unsatisfied'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Unsatisfied xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('unsatisfied');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has not satisfied the object. If a target is specified, it indicates the context within which the object was not satisfied. For instance, if a person fails to satisfy the requirements of some particular challenge, the person is the actor; the requirement is the object and the challenge is the target.');
		  },
		  typeblock: [{ translatedName: 'unsatisfiedBlock' }]
};


Blockly.Blocks['unsaved'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Unsaved xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('unsaved');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has removed the object from the collection of saved items.');
		  },
		  typeblock: [{ translatedName: 'unsavedBlock' }]
};


Blockly.Blocks['unshared'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Unshared xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('unshared');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor is no longer sharing the object. If a target is specified, it indicates the entity with whom the object is no longer being shared.');
		  },
		  typeblock: [{ translatedName: 'unsharedBlock' }]
};


Blockly.Blocks['updated'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Updated xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('updated');
		    this.setOutput(true);
		    this.setTooltip('The \"update\" verb indicates that the actor has modified the object. Use of the \"update\" verb is generally reserved to indicate modifications to existing objects or data such as changing an existing users profile information.');
		  },
		  typeblock: [{ translatedName: 'updatedBlock' }]
};


Blockly.Blocks['upvoted'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Up voted xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('up voted');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has voted up for a specific object. This is analogous to giving a thumbs up.');
		  },
		  typeblock: [{ translatedName: 'upvotedBlock' }]
};


Blockly.Blocks['used'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Used xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('used');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has used the object in some manner.');
		  },
		  typeblock: [{ translatedName: 'usedBlock' }]
};


Blockly.Blocks['viewed'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Viewed xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('viewed');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has viewed the object.');
		  },
		  typeblock: [{ translatedName: 'viewedBlock' }]
};


Blockly.Blocks['voided'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Voided xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('voided');
		    this.setOutput(true);
		    this.setTooltip('A special LRS-reserved verb. Used by the LRS to declare that an activity statement is to be voided from record.');
		  },
		  typeblock: [{ translatedName: 'voidedBlock' }]
};


Blockly.Blocks['voteddown'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Voted down (with reason) xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('voted down (with reason)');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has voted down a discussion post and given a reason. The reason is stored in the Result Response and can be "Rude or Inappropriate" or some other value. Consider using http://id.tincanapi.com/verb/voted-down for down votes that do not fit this pattern.');
		  },
		  typeblock: [{ translatedName: 'voteddownBlock' }]
};


Blockly.Blocks['votedup'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Voted up (with reason) xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('voted up (with reason)');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has voted up a discussion post and given a reason. The reason is stored in the Result Response and can be \"Started Discussion\", \"Developed Discussion", "Resolved Discussion\" or some other value. Consider using http://id.tincanapi.com/verb/voted-up for up votes that do not fit this pattern.');
		  },
		  typeblock: [{ translatedName: 'votedupBlock' }]
};


Blockly.Blocks['walked'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Walked xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('walked');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor walked a distance indicated by the activity.');
		  },
		  typeblock: [{ translatedName: 'walkedBlock' }]
};


Blockly.Blocks['wasassignedjobtitle'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Was assigned job title xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('was assigned job title');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor was assigned the job title represented by the object of the statement. This object should use the activity type http://id.tincanapi.com/activitytype/job-title. This verb is used any time when the persons job title changes, for example when they are first hired and any time they are promoted, demoted or otherwise change job.');
		  },
		  typeblock: [{ translatedName: 'wasassignedjobtitleBlock' }]
};


Blockly.Blocks['wasat'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Was at xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('was at');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor was located at the object. For instance, a person being at a specific physical location.');
		  },
		  typeblock: [{ translatedName: 'wasatBlock' }]
};


Blockly.Blocks['washiredby'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Was hired by xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('was hired by');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor was hired by the organization represented by the object of the statement.');
		  },
		  typeblock: [{ translatedName: 'washiredbyBlock' }]
};


Blockly.Blocks['watched'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Watched xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('watched');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has watched the object. This verb is typically applicable only when the object represents dynamic, visible content such as a movie, a television show or a public performance. This verb is a more specific form of the verbs \"experience\", \"play\" and \"consume\".');
		  },
		  typeblock: [{ translatedName: 'watchedBlock' }]
};


Blockly.Blocks['won'] = {
		  category: "xAPIverbs",
		  helpUrl: 'Won xAPI verb',
		  init: function() {
			this.setColour(Blockly.XAPIVERBS_CATEGORY_HUE);
		    this.appendDummyInput().appendField('won');
		    this.setOutput(true);
		    this.setTooltip('Indicates that the actor has won the object. This verb is typically applicable only when the object represents some form of competition, such as a game.');
		  },
		  typeblock: [{ translatedName: 'wonBlock' }]
};